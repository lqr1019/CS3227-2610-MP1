package budgetwise;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import budgetwise.model.CategoryCatalog;
import budgetwise.service.TransactionStore;
import budgetwise.ui.MainView;

/** Entry point for the BudgetWise desktop application. */
public final class BudgetApp extends Application {

    private static final String WINDOW_TITLE = "BudgetWise";
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 600;

    /** Starts the initial application shell. */
    @Override
    public void start(Stage stage) {
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(new MainView(new TransactionStore(), new CategoryCatalog()), WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();
    }

    /** Launches JavaFX. */
    public static void main(String[] args) {
        launch(args);
    }
}
