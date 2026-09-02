# BudgetWise

BudgetWise is an offline JavaFX desktop application for students and employees
to record transactions, manage budgets, and understand their spending.

## Current release: V1.0

BudgetWise currently supports:

- Recording income and expenses with amount, date, category, payment method,
  and notes.
- Built-in and custom categories.
- Transaction history with search, filters, editing, and deletion.
- Monthly overall and category budgets with spending and remaining amounts.
- Monthly dashboard totals and daily expense trends.
- Category, daily, weekly, and monthly spending reports.
- Local persistence under `data/budgetwise-data.ser`.

The application works offline and stores data locally without a database.

## Requirements

- Java Development Kit 25
- macOS, Windows, or Linux with a graphical desktop environment

## Run and test

From the project root:

```text
./gradlew run
./gradlew test
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

The first Gradle run downloads build dependencies. Application data is saved
automatically in the `data/` directory relative to the application working
directory and is loaded when the application starts.

## Build a JAR

```text
./gradlew clean jar
```

The generated artifact is placed in `build/libs/budgetwise-V1.0.jar`.
The JAR includes the `budgetwise.Launcher` main-class entry and JavaFX runtime
dependencies. Run it with:

```text
java -jar build/libs/budgetwise-V1.0.jar
```

The JAR is platform-specific because JavaFX includes native runtime files. If
you build on another operating system, build the JAR again on that platform.

See [docs/UserGuide.md](docs/UserGuide.md) for setup and usage instructions and
[docs/DeveloperGuide.md](docs/DeveloperGuide.md) for the design.
