# BudgetWise Developer Guide

## Release 0.1.0

BudgetWise is an offline Java SE 25 desktop application built with JavaFX and
Gradle. The repository currently contains an application shell and a JUnit 5
test. There is no persistence layer yet; future releases will keep application
data in memory as requested.

## Architecture

The application currently has one entry-point class, `budgetwise.BudgetApp`,
which extends JavaFX `Application` and creates the primary stage. The UI is
intentionally a small shell so domain, service, and presentation components can
be introduced independently in later steps.

The intended layers are:

- model: transaction, category, budget, and reporting data structures;
- service: validation, filtering, budget calculations, and report aggregation;
- UI: JavaFX views and controllers;
- test: unit tests for model/service behavior and focused UI tests where useful.

## Build and test process

The Gradle build targets Java 25 through the Java toolchain configuration and
uses JavaFX Controls. JUnit 5 is used for automated tests. Run `gradle test`
for tests and `gradle run` to launch the desktop application until the Gradle
wrapper is added. Each feature must add or update tests in the same
implementation step. Documentation and summary logs are updated with each
release step.

## Coding standards

Code uses four-space indentation, explicit package declarations, descriptive
names, Javadoc for public APIs, small cohesive classes, and tests named after
the behavior they verify. Business logic should remain independent of JavaFX
where practical so it can be tested without launching a graphical window.

## Acknowledgements

The product requirements, workflow constraints, and documentation requirements
were supplied by the project owner. The initial project structure and source
files were created for this project. No external code, documentation, or
design was reused in release 0.1.0.
