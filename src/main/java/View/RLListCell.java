package View;

import Objects.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class RLListCell extends ListCell<Stream> {

    private final GridPane grid = new GridPane();
    private final Circle circle = new Circle(7);
    private final Label name = new Label();
    private final Label views = new Label();

    public RLListCell() {
        super();
        configureGrid();
        addControlsToGrid();
        configureLabel();
    }

    private void configureLabel() {
        name.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        views.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
    }

    private void configureGrid() {
        grid.setHgap(10);
        grid.setVgap(0);
        grid.setPadding(new Insets(5, 10, 5, 10));
    }

    private void addControlsToGrid() {
        grid.add(circle, 0, 0, 1, 2);
        grid.add(name, 1, 0);
        grid.add(views, 1, 1);
    }

    @Override
    protected void updateItem(Stream item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            if (item.isLive()) {
                setStatusLive(item);
            } else {
                setStatusDown(item);
            }
            setGraphic(grid);
        }
    }

    private void setStatusDown(Stream item) {
        circle.setFill(Color.RED);
        name.setText(item.getName());
        views.setText("Down");
    }

    private void setStatusLive(Stream item) {
        circle.setFill(Color.GREEN);
        name.setText(item.getName());
        views.setText(String.valueOf(item.getViews()));
    }

}
