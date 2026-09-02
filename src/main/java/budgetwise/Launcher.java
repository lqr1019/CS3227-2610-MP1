package budgetwise;

import javafx.application.Application;

/** Non-JavaFX bootstrap entry point for executable JAR launches. */
public final class Launcher {

    private Launcher() {
        // Utility class.
    }

    /** Launches the JavaFX application. */
    public static void main(String[] args) {
        Application.launch(BudgetApp.class, args);
    }
}
