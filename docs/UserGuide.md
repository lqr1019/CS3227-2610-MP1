# BudgetWise User Guide

## Current release

This guide describes release 0.7.0. The current release provides JavaFX
transaction-entry, history, budget-management, monthly dashboard, and reports
screens.

## Requirements

- Java Development Kit (JDK) 25.
- The included Gradle wrapper.
- macOS, Windows, or Linux with a graphical desktop environment.

The application is designed to work offline. All transaction and category data
is held in memory and is lost when the application closes.

## Setup and test

From the project root, run:

```text
./gradlew test
./gradlew run
```

The first Gradle run downloads build dependencies from Maven Central, so
network access is required for initial setup. Subsequent application use is
offline.

## Using the current release

Run the application using the command above. The “Transaction” panel accepts
income or expense type, a positive amount, an ISO date (`YYYY-MM-DD`), a
category, payment method, and optional notes. Select “Add transaction” to add
the record. Select a row to edit it, then select “Save changes”; select
“Delete selected” to remove it. “Clear” resets the form.

Use “New category” to create a custom category. The history panel can search
category, payment method, and notes, and can filter by type or category. History
is shown newest first. Invalid amount, date, type, or required fields produce
an error dialog identifying the invalid field, such as “Payment method cannot
be empty”.

Open the “Budgets” tab to add a monthly budget. Leave the category as “All
categories” for an overall budget or select a category for a category budget.
Enter the month as `YYYY-MM` and a positive limit. The table shows the matching
expense total and remaining amount; a negative remaining amount means the
budget is overspent. Select a budget and choose “Delete selected” to remove it.

Open the “Dashboard” tab to select a month using `YYYY-MM` and view total
income, total expenses, balance, and a daily expense line chart. Select
“Refresh” after changing the month.

Open the “Reports” tab to choose spending by category, day, week, or month.
Category reports use a pie chart; day reports use a line chart; week and month
reports use bar charts. The selected month applies to category, day, and week
reports. Month reports include every month represented by recorded expenses.
