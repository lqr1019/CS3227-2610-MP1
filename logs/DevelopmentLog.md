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
