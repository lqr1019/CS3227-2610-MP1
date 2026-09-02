# BudgetWise Development Log

This file is the consolidated summary of prompts and interactions during
development. It is updated as the product evolves.

## Initial requirements

The project owner requested an offline Java desktop budget-management app for
students and employees, using Java SE 25, Gradle, JavaFX, and in-memory data.
Requested product areas are transactions, categories, history, budgets,
monthly dashboard metrics, and charts/reports. The owner also requested
feature tests, coding standards, three maintained documents, interaction
summaries, and incremental implementation with a review/commit checkpoint after
each step.

## Clarification interaction

The project manager clarification established JavaFX, no database, offline use,
and macOS, Windows, and Linux support. No mockups, sample data, or additional
styling requirements were provided.

## Step 1 interaction and outcome

The repository was empty except for its README. The project foundation was
created with Gradle configuration, a JavaFX shell, a baseline JUnit test, and
the required guides. The environment was found to have Java 17 and no system
Gradle, so full execution was not claimed. The owner requested removal of a
separate step-specific log, so summaries are kept in this consolidated file.

## Step 2 interaction and outcome

The owner requested proceeding to Step 2. The domain foundation was added with
validated immutable transactions, transaction types, built-in/custom
categories, an in-memory category catalog, and unit tests for validation,
lookup, and duplicate handling.

## Step 3 interaction and outcome

The owner requested proceeding to Step 3. The transaction-history service was
added with in-memory CRUD operations, newest-first ordering, text search,
type/category filters, inclusive date ranges, validation, and unit tests.

## Step 4 interaction and outcome

The owner requested the next step. A JavaFX transaction-entry and history view
was added with add/edit/delete actions, custom-category creation, text search,
type/category filters, and error dialogs. A form conversion service and unit
tests cover parsing and validation. The full Gradle test suite passed with Java
25 after correcting one compile-time exception declaration and a JavaFX generic
warning.

## Category display correction interaction

The owner reported that category dropdowns displayed the record representation
instead of only the category name. `Category.toString()` now returns the name,
and a regression assertion verifies the display value.

## Step 5 interaction and outcome

The owner requested the next step. Monthly overall and category budgets were
added with in-memory storage, duplicate-scope validation, spending and
remaining calculations, a JavaFX budget tab, and unit tests.

## Step 6 interaction and outcome

The owner requested the next step. Monthly dashboard summaries were added with
income, expenses, balance, daily expense aggregation, a JavaFX line chart, and
unit tests.

## Step 7 interaction and outcome

The owner requested the next step. Report aggregation by category, day, week,
and month was added with a JavaFX Reports tab using pie, line, and bar charts,
plus unit tests.

## Validation correction interaction

The owner reported that the displayed error did not match an empty payment
field. The form service was changed to preserve field-specific messages, and a
regression test now verifies the exact payment-method message.

## Local persistence interaction

The owner requested that application data remain available after restarting the
app. Java built-in object serialization now stores transactions, categories,
and budgets in `data/budgetwise-data.ser`, with a temporary-directory reload
test.

## Version naming interaction

The owner requested the project version name be changed to `V1.0`. Gradle and
the maintained project guides now use `V1.0` consistently.

## Toolchain interaction

The owner requested installation of Java 25 and Gradle. Homebrew installed
OpenJDK 25.0.4.1 and Gradle 9.7.1. Running the test suite exposed and resolved
a Gradle settings/project repository conflict and a missing JUnit Platform
launcher runtime dependency. The project test suite now completes
successfully with Java 25.

## Step 8 interaction and outcome

The owner requested the next step. Integration polish synchronized newly added
categories with the budget selector, added precise empty-date validation and a
regression test, and added the Gradle wrapper for peer setup.

## JAR runtime correction interaction

The owner reported that the JAR could not load JavaFX classes. The Gradle JAR
task now bundles runtime dependencies while retaining the main-class manifest.
The rebuilt JAR contains both `budgetwise.BudgetApp` and
`javafx.application.Application`.

The owner then reported JavaFX’s missing-runtime message when launching the
fat JAR. The manifest now starts a non-`Application` `budgetwise.Launcher`,
which bootstraps `BudgetApp` and avoids JavaFX’s special module-path launcher
check.

CI testing then showed that the launcher extracted native platform JARs but
omitted the shared JavaFX class JARs. The selection now extracts both shared
JavaFX libraries and only the matching platform-native libraries.

## JavaFX platform correction interaction

The owner reported that `./gradlew run` selected an incompatible Windows
x86_64 JavaFX native library on an Apple Silicon Mac. The build now selects a
single JavaFX classifier based on the host operating system, so macOS uses the
macOS native runtime and Windows/Linux builds select their own runtime.

The owner reported that the Shadow JAR still crashed on Windows. Shadow JAR
was removed; the project now uses the standard JavaFX Gradle plugin and
platform-specific `installDist` distributions, which keep JavaFX native files
outside the application JAR.

## Direct JAR launch requirement interaction

The owner clarified that peer users will receive only a JAR and will run it
with `java -jar` from an otherwise empty folder. The standard `jar` task now
bundles the runtime classpath and uses the non-Application launcher, producing
a platform-specific self-contained JAR. Each target OS/architecture must have
its own build.

The runtime-selection implementation was completed without Shadow JAR. The
executable JAR stores platform JavaFX artifacts as nested JARs and the pure-JDK
launcher extracts only the matching OS/architecture artifacts before starting
JavaFX.

The owner then reported that `./gradlew run` still selected the Windows runtime.
The Gradle `run` task now filters its classpath to the host OS/architecture as
well, while direct JAR launches continue to select their runtime dynamically.

The JavaFX dependency is aligned to version 25.0.1 to match the project's
Java 25 toolchain. JavaFX 25 requires JDK 23 or newer.
