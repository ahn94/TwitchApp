package View;

import Domain.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private TextField txtUpdateInterval;
    @FXML
    private ChoiceBox<String> chcGame;
    @FXML
    private CheckBox chkNotifications;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnCancel;

    private Settings settings = Settings.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUpdateInterval();
        initializeGame();
        initializeNotification();
    }

    private void initializeUpdateInterval() {
        txtUpdateInterval.setText(settings.getUpdateInterval().toString());
        txtUpdateInterval.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                keyEvent.consume();
            }
        });
    }

    private void initializeGame() {
        chcGame.getItems().setAll(settings.getGames());
        chcGame.getSelectionModel().select(settings.getGame());
    }

    private void initializeNotification() {
        chkNotifications.setSelected(settings.isNotifications());
    }

    @FXML
    private void confirmClicked(ActionEvent event) {
        //update interval setting
        int value = Integer.parseInt(txtUpdateInterval.getText());
        settings.setUpdateInterval(value);

        //stream game setting
        settings.setGame(chcGame.getSelectionModel().getSelectedItem());

        //stream online notification toggle
        settings.setNotifications(chkNotifications.isSelected());

        //save to file
        settings.save();

        closeSettingsWindow();
    }

    @FXML
    private void cancelClicked(ActionEvent event) {
        closeSettingsWindow();
    }

    private void closeSettingsWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
