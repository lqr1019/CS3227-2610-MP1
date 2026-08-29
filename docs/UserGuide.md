# BudgetWise User Guide

## Current release

This guide describes release 0.1.0. The current release provides the
BudgetWise desktop application shell only. It displays the application name
and a welcome message in a JavaFX window. Transaction recording, categories,
history, budgets, dashboards, charts, and reports are not available yet and
will be documented here as they are implemented.

## Requirements

- Java Development Kit (JDK) 25.
- Gradle 8.14 or newer. A Gradle wrapper will be added before the first peer
  testing release.
- macOS, Windows, or Linux with a graphical desktop environment.

The application is designed to work offline. It currently stores no user data.

## Setup and test

From the project root, run:

```text
gradle test
gradle run
```

The first Gradle run downloads build dependencies from Maven Central, so
network access is required for initial setup. Subsequent application use is
offline.

## Using the current release

Run the application using the command above. A window titled “BudgetWise”
appears with a welcome message. Close the window using the operating system
window controls.
