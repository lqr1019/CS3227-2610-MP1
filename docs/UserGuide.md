# BudgetWise User Guide

## Current release

This guide describes release 0.2.0. The current release provides the
BudgetWise desktop application shell and its in-memory transaction/category
domain foundation. The visible application still displays only the application
name and a welcome message in a JavaFX window. Transaction recording, history,
budgets, dashboards, charts, and reports are not available from the UI yet.

## Requirements

- Java Development Kit (JDK) 25.
- Gradle 8.14 or newer. A Gradle wrapper will be added before the first peer
  testing release.
- macOS, Windows, or Linux with a graphical desktop environment.

The application is designed to work offline. The current domain objects hold
data only while the application is running; the UI does not yet create or
display transactions.

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
