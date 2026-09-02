package budgetwise;

import javafx.application.Application;

/** JavaFX entry point invoked after the platform runtime has been selected. */
public final class JavaFxLauncher {

    private JavaFxLauncher() {
        // Utility class.
    }

    /** Launches the JavaFX application. */
    public static void main(String[] args) {
        Application.launch(BudgetApp.class, args);
    }
}
