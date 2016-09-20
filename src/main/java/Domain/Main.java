package Domain;

import View.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.IOException;


public class Main extends Application{

    private Parent root;

    private Logger log = (Logger) LogManager.getLogger(this.getClass().getName());


    private double xOffset;
    private double yOffset;
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("app.fxml"));
            loader.setRoot(root);
            loader.setController(new AppController());
            root = loader.load();
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            setListener(primaryStage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setListener(Stage primaryStage) {
        root.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
