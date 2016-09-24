package View;

import Domain.Settings;
import Domain.TwitchApi;
import Objects.Stream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.controlsfx.control.Notifications;
import rx.Observable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class AppController {

    public Button settingBtn;
    public Label topGame;
    private Settings settings;
    public Button refresh;
    public Button minimize;
    public Button exit;
    public ListView<Stream> topStreamsList;
    public ListView<Stream> followedStreamsList;
    private ObservableList<Stream> topStreams = FXCollections.observableArrayList();
    private ObservableList<Stream> followedStreams = FXCollections.observableArrayList();


    private Logger log = (Logger) LogManager.getLogger(AppController.class.getName());
    private BehaviorSubject<Integer> timerSubject;
    @Inject TwitchApi twitchApi;
    private boolean validToken;


    @FXML
    public void initialize() {
        ApplicationComponent appComponent = DaggerApplicationComponent.create();
        appComponent.inject(this);
        settings = Settings.getInstance();

        configureButtons();
        initTwitchApi();

        configureList(topStreamsList, topStreams);
        configureList(followedStreamsList, followedStreams);

        twitchApi.getGamesList(100)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(topGames -> settings.setGames(topGames.getGames()));

        timerSubject = BehaviorSubject.create(settings.getUpdateInterval());

        settings.updateIntervalProperty().addListener((observable, oldValue, newValue) -> {
            timerSubject.onNext(newValue.intValue());
        });

        Observable<Integer> timer = timerSubject
                .switchMap(interval -> Observable.timer(0, interval, TimeUnit.MINUTES))
                .map(time_sec -> settings.getUpdateInterval());

        timer.flatMap(integer -> twitchApi.getTopStreamsForGame(settings.getGame(), 10))
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(streamList -> refreshTopStreams(streamList.getStreams()));

        timer.flatMap(integer -> twitchApi.getFollowedStreams(settings.getAuthToken(), 20, 0))
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(streamList -> refreshFollowed(streamList.getStreams()));
    }

    private void initTwitchApi() {
        if (settings.getAuthToken() == null) {
            String authToken = authenticate();
            settings.setAuthToken(authToken);
        }
        validToken = false;
        twitchApi.getFollowedStreams(settings.getAuthToken(), 10, 0)
                .doOnError(throwable -> validToken = false)
                .doOnNext(streamList -> validToken = true);
        if (validToken) {
            String authToken = authenticate();
            settings.setAuthToken(authToken);
        }
        settings.save();
    }


    private void refreshTopStreams(List<Stream> newStreamList) {
        topGame.setText(settings.getGame());
        topStreams.clear();
        topStreams.addAll(newStreamList);
    }

    private void refreshFollowed(List<Stream> streams) {
        log.debug("refreshing followed streams");
        List<Stream> oldStreamList = new ArrayList<>();
        oldStreamList.addAll(followedStreams);
        followedStreams.clear();
        followedStreams.addAll(streams);
        showNotifications(oldStreamList, followedStreamsList);
    }

    private void showNotifications(List<Stream> oldStreamList, ListView<Stream> listView) {
        if (settings.isNotifications()) {
            log.debug("old list: " + listView.getItems());
            listView.getItems().stream()
                    .filter(s -> s != null)
                    .filter(s -> !oldStreamList.contains(s))
                    .forEach(s -> Notifications.create()
                            .title("Twitch")
                            .text(s.getName() + " just went live!")
                            .hideAfter(Duration.seconds(20))
                            .onAction(event -> openUrl(s.getUrl()))
                            .darkStyle()
                            .show()
                    )
            ;
        }
    }

    private String authenticate() {
        WebView webView = new WebView();
        webView.getEngine().load(Settings.AUTH_URL);

        Stage popup = new Stage();
        popup.setScene(new Scene(webView));
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(new Stage());

        StringBuilder result = new StringBuilder();
        webView.getEngine().locationProperty().addListener((observableValue, ov, url) -> {
            if (url.startsWith("http://localhost")) {
                String token = StringUtils.substringBetween(url, "=", "&");
                result.append(token);
                System.out.println(token);
                popup.close();
            }
        });
        popup.showAndWait();
        return result.toString();
    }

    private void configureList(ListView<Stream> listView, ObservableList<Stream> list) {
        listView.setCellFactory(param -> new RLListCell());
        listView.setItems(list);
        addClickListener(listView);
        listView.setFixedCellSize(46);
    }

    private void configureButtons() {
        refresh.setOnMouseClicked(event -> timerSubject.onNext(settings.getUpdateInterval()));
        exit.setOnAction(event -> Platform.exit());
        minimize.setOnAction(event -> {
            Stage stage = (Stage) minimize.getScene().getWindow();
            stage.setIconified(true);
        });
        settingBtn.setOnAction(this::openSettingsWindow);
    }

    private void addClickListener(ListView<Stream> currentList) {
        EventHandler<MouseEvent> handler = event -> {
            if (event.getClickCount() == 2) {
                Stream selected = currentList.getSelectionModel().getSelectedItem();
                openUrl(selected.getUrl());
            }
        };
        currentList.setOnMouseClicked(handler);
    }

    @FXML
    private void openSettingsWindow(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/settings.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Settings");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUrl(String url) {
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    URI uri = new URI(url);
                    desktop.browse(uri);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
