package View;

import Domain.FxScheduler;
import Domain.Settings;
import Domain.Twitch;
import Objects.Stream;
import javafx.animation.Timeline;
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

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AppController {


    public Button settingBtn;
    public Label topGame;
    private Settings settings;
    private Twitch twitch;
    public Button refresh;
    public Button minimize;
    public Button exit;
    public ListView<Stream> topStreamsList;
    public ListView<Stream> followedStreamsList;
    private ObservableList<Stream> topStreams = FXCollections.observableArrayList();
    private ObservableList<Stream> followedStreams = FXCollections.observableArrayList();
    private Timeline scheduledRefresh;

    private Logger log = (Logger) LogManager.getLogger(AppController.class.getName());

    @FXML
    public void initialize() {
        settings = Settings.getInstance();
        //refresh streams periodically
        scheduledRefresh = FxScheduler.schedule(Duration.minutes(settings.getUpdateInterval()), this::refreshLists);
        //add handler to reschedule stream refreshing when interval changes
        settings.updateIntervalProperty().addListener((observableValue, ov, nv) -> {
            scheduledRefresh.stop();
            scheduledRefresh = FxScheduler.schedule(Duration.minutes(nv.doubleValue()), this::refreshLists);
        });
        initTwitchApi();
        configureButtons();
        configureList(topStreamsList, topStreams);
        configureList(followedStreamsList, followedStreams);

        settings.setGames(twitch.getGamesList());
        Platform.runLater(this::refreshLists);
    }

    private void initTwitchApi() {
        if (settings.getAuthToken() == null) {
            String authToken = authenticate();
            settings.setAuthToken(authToken);
        }
        twitch = new Twitch(settings.getAuthToken());
        if (!twitch.isAuthValid()) {
            String authToken = authenticate();
            settings.setAuthToken(authToken);
            twitch.setAuthToken(authToken);
        }
        settings.save();
    }

    private void refreshLists() {
        topGame.setText(settings.getGame());
        refreshFollowed();
        refreshTopStreams();
    }

    private void refreshTopStreams() {
        topStreams.clear();
        try {
            List<Stream> newStreamList = twitch.getRLStreams();
            log.debug("new list: " + Arrays.deepToString(newStreamList.toArray()));
            topStreams.addAll(newStreamList);
        } catch (SocketTimeoutException e) {
            log.info("Twitch API timeout");
        }
    }

    private void refreshFollowed() {
        log.debug("refreshing followed streams");
        List<Stream> oldStreamList = new ArrayList<>();
        List<Stream> newStreamList;
        oldStreamList.addAll(followedStreams);
        followedStreams.clear();
        try {
            newStreamList = twitch.getFollowedStreams();
            log.debug("new list: " + Arrays.deepToString(newStreamList.toArray()));
            followedStreams.addAll(newStreamList);
        } catch (SocketTimeoutException e) {
            log.info("Twitch API timeout");
        }
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
                            .onAction(event -> twitch.openUrl(s.getUrl()))
                            .darkStyle()
                            .show()
                    )
            ;
        }
    }

    private String authenticate() {
        WebView webView = new WebView();
        webView.getEngine().load(Twitch.AUTH_URL);

        Stage popup = new Stage();
        popup.setScene(new Scene((webView)));
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
        refresh.setOnMouseClicked(event -> refreshLists());
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
                twitch.openUrl(selected.getUrl());
            }
        };
        currentList.setOnMouseClicked(handler);
    }

    @FXML
    private void openSettingsWindow(ActionEvent event){
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

}
