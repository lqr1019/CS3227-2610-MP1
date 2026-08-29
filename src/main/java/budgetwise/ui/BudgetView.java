package budgetwise.ui;

import budgetwise.model.Budget;
import budgetwise.model.Category;
import budgetwise.service.BudgetCalculator;
import budgetwise.service.BudgetStore;
import budgetwise.service.TransactionStore;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** JavaFX view for creating and reviewing monthly budgets. */
@SuppressWarnings("unchecked")
public final class BudgetView extends BorderPane {

    private final BudgetStore budgetStore;
    private final TransactionStore transactionStore;
    private final BudgetCalculator calculator = new BudgetCalculator();
    private final TextField monthField = new TextField(YearMonth.now().toString());
    private final TextField limitField = new TextField();
    private final ComboBox<Category> categoryField = new ComboBox<>();
    private final TableView<Budget> table = new TableView<>();

    /** Creates a budget view backed by the supplied in-memory stores. */
    public BudgetView(BudgetStore budgetStore, TransactionStore transactionStore, Category[] categories) {
        this.budgetStore = budgetStore;
        this.transactionStore = transactionStore;
        categoryField.getItems().setAll(categories);
        categoryField.getItems().add(0, null);
        categoryField.setValue(null);
        categoryField.setButtonCell(new javafx.scene.control.ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "All categories" : item.name());
            }
        });
        setPadding(new Insets(0, 0, 0, 0));
        setTop(buildControls());
        setCenter(buildTable());
        refresh();
    }

    /** Refreshes budget usage after a transaction changes. */
    public void refresh() {
        table.setItems(FXCollections.observableArrayList(budgetStore.all()));
    }

    /** Replaces the category choices after a custom category is created. */
    public void updateCategories(Category[] categories) {
        Category selected = categoryField.getValue();
        categoryField.getItems().setAll(Arrays.asList(categories));
        categoryField.getItems().add(0, null);
        categoryField.setValue(selected);
    }

    private HBox buildControls() {
        monthField.setPromptText("YYYY-MM");
        limitField.setPromptText("Limit");
        Button addButton = new Button("Add budget");
        addButton.setOnAction(event -> addBudget());
        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteBudget());
        HBox controls = new HBox(8, new Label("Month"), monthField, categoryField,
                new Label("Limit"), limitField, addButton, deleteButton);
        controls.setPadding(new Insets(0, 0, 12, 0));
        return controls;
    }

    private TableView<Budget> buildTable() {
        table.getColumns().setAll(
                column("Month", budget -> budget.month().toString()),
                column("Category", Budget::scopeName),
                column("Limit", budget -> budget.limit().toPlainString()),
                column("Spent", budget -> calculator.spent(budget, transactionStore.all()).toPlainString()),
                column("Remaining", budget -> calculator.remaining(budget, transactionStore.all()).toPlainString()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        return table;
    }

    private TableColumn<Budget, String> column(String title, java.util.function.Function<Budget, String> value) {
        TableColumn<Budget, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(value.apply(cell.getValue())));
        return column;
    }

    private void addBudget() {
        try {
            budgetStore.add(Budget.create(YearMonth.parse(monthField.getText().trim()),
                    categoryField.getValue(), new BigDecimal(limitField.getText().trim())));
            limitField.clear();
            refresh();
        } catch (RuntimeException exception) {
            new Alert(Alert.AlertType.ERROR, "Enter a valid month (YYYY-MM) and positive limit.", ButtonType.OK)
                    .showAndWait();
        }
    }

    private void deleteBudget() {
        Budget selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            budgetStore.delete(selected.id());
            refresh();
        }
    }
}
