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

## Toolchain interaction

The owner requested installation of Java 25 and Gradle. Homebrew installed
OpenJDK 25.0.4.1 and Gradle 9.7.1. Running the test suite exposed and resolved
a Gradle settings/project repository conflict and a missing JUnit Platform
launcher runtime dependency. The project test suite now completes
successfully with Java 25.
