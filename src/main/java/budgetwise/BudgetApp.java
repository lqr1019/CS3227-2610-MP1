package budgetwise;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Entry point for the BudgetWise desktop application. */
public final class BudgetApp extends Application {

    private static final String WINDOW_TITLE = "BudgetWise";
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 600;

    /** Starts the initial application shell. */
    @Override
    public void start(Stage stage) {
        Label placeholder = new Label("BudgetWise\nYour personal budget companion");
        placeholder.setStyle("-fx-font-size: 24px; -fx-text-alignment: center;");

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(new StackPane(placeholder), WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();
    }

    /** Launches JavaFX. */
    public static void main(String[] args) {
        launch(args);
    }
}
