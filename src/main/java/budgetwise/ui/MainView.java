package budgetwise.ui;

import budgetwise.model.Category;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import budgetwise.service.TransactionFormService;
import budgetwise.service.BudgetStore;
import budgetwise.service.PersistenceService;
import java.nio.file.Path;
import budgetwise.service.TransactionStore;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/** Main transaction-entry and transaction-history view. */
@SuppressWarnings("unchecked")
public final class MainView extends BorderPane {

    private final TransactionStore store;
    private final budgetwise.model.CategoryCatalog categoryCatalog;
    private final TransactionFormService formService = new TransactionFormService();
    private final BudgetStore budgetStore;
    private final PersistenceService persistenceService;
    private final Path dataDirectory;
    private final ComboBox<String> typeField = new ComboBox<>();
    private final TextField amountField = new TextField();
    private final DatePicker dateField = new DatePicker(LocalDate.now());
    private final ComboBox<Category> categoryField = new ComboBox<>();
    private final TextField paymentMethodField = new TextField();
    private final TextField notesField = new TextField();
    private final TextField searchField = new TextField();
    private final ComboBox<String> typeFilter = new ComboBox<>();
    private final ComboBox<Category> categoryFilter = new ComboBox<>();
    private final TableView<Transaction> historyTable = new TableView<>();
    private final Button saveButton = new Button("Add transaction");
    private BudgetView budgetView;
    private DashboardView dashboardView;
    private ReportView reportView;
    private Transaction selectedTransaction;

    /** Creates a view backed by the supplied in-memory stores. */
    public MainView(TransactionStore store, budgetwise.model.CategoryCatalog categoryCatalog,
            BudgetStore budgetStore, PersistenceService persistenceService, Path dataDirectory) {
        this.store = Objects.requireNonNull(store, "store");
        this.categoryCatalog = Objects.requireNonNull(categoryCatalog, "categoryCatalog");
        this.budgetStore = Objects.requireNonNull(budgetStore, "budgetStore");
        this.persistenceService = Objects.requireNonNull(persistenceService, "persistenceService");
        this.dataDirectory = Objects.requireNonNull(dataDirectory, "dataDirectory");
        configureFields();
        setPadding(new Insets(18));
        setTop(buildHeader());
        setLeft(buildForm());
        setCenter(buildTabs());
        refreshCategories();
        refreshHistory();
    }

    private Node buildTabs() {
        budgetView = new BudgetView(budgetStore, store, categoryCatalog.all().toArray(Category[]::new), this::persist);
        dashboardView = new DashboardView(store);
        reportView = new ReportView(store);
        Tab historyTab = new Tab("Transactions", buildHistory());
        Tab budgetTab = new Tab("Budgets", budgetView);
        Tab dashboardTab = new Tab("Dashboard", dashboardView);
        Tab reportTab = new Tab("Reports", reportView);
        historyTab.setClosable(false);
        budgetTab.setClosable(false);
        dashboardTab.setClosable(false);
        reportTab.setClosable(false);
        return new TabPane(dashboardTab, historyTab, budgetTab, reportTab);
    }

    private void configureFields() {
        typeField.getItems().setAll("Income", "Expense");
        typeField.setValue("Expense");
        typeFilter.getItems().setAll("All types", "Income", "Expense");
        typeFilter.setValue("All types");
        categoryField.setConverter(categoryConverter());
        categoryFilter.setConverter(categoryConverter());
        searchField.setPromptText("Search history");
        amountField.setPromptText("e.g. 12.50");
        dateField.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : date.toString();
            }

            @Override
            public LocalDate fromString(String value) {
                return value == null || value.isBlank() ? null : LocalDate.parse(value);
            }
        });
        saveButton.setOnAction(event -> saveTransaction());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> refreshHistory());
        typeFilter.valueProperty().addListener((observable, oldValue, newValue) -> refreshHistory());
        categoryFilter.valueProperty().addListener((observable, oldValue, newValue) -> refreshHistory());
    }

    private Node buildHeader() {
        Label title = new Label("BudgetWise");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        Label subtitle = new Label("Record transactions and review your history");
        VBox header = new VBox(4, title, subtitle);
        header.setPadding(new Insets(0, 0, 18, 0));
        return header;
    }

    private Node buildForm() {
        Label title = new Label("Transaction");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        GridPane fields = new GridPane();
        fields.setHgap(8);
        fields.setVgap(8);
        fields.addRow(0, new Label("Type"), typeField);
        fields.addRow(1, new Label("Amount"), amountField);
        fields.addRow(2, new Label("Date"), dateField);
        fields.addRow(3, new Label("Category"), categoryField);
        fields.addRow(4, new Label("Payment"), paymentMethodField);
        fields.addRow(5, new Label("Notes"), notesField);
        Button customCategoryButton = new Button("New category");
        customCategoryButton.setOnAction(event -> addCustomCategory());
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(event -> clearForm());
        HBox actions = new HBox(8, saveButton, clearButton, customCategoryButton);
        actions.setAlignment(Pos.CENTER_LEFT);
        VBox form = new VBox(12, title, fields, actions);
        form.setPrefWidth(390);
        form.setPadding(new Insets(0, 18, 0, 0));
        return form;
    }

    private Node buildHistory() {
        Label title = new Label("Transaction history");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox filters = new HBox(8, searchField, typeFilter, categoryFilter);
        TableColumn<Transaction, String> date = column("Date", transaction -> transaction.date().toString());
        TableColumn<Transaction, String> type = column("Type", transaction -> transaction.type().name());
        TableColumn<Transaction, String> amount = column("Amount", transaction -> transaction.amount().toPlainString());
        TableColumn<Transaction, String> category = column("Category", transaction -> transaction.category().name());
        TableColumn<Transaction, String> payment = column("Payment", Transaction::paymentMethod);
        TableColumn<Transaction, String> notes = column("Notes", Transaction::notes);
        historyTable.getColumns().setAll(date, type, amount, category, payment, notes);
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        historyTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadSelection(newValue));
        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteSelected());
        VBox history = new VBox(12, title, filters, historyTable, deleteButton);
        VBox.setVgrow(historyTable, javafx.scene.layout.Priority.ALWAYS);
        return history;
    }

    private TableColumn<Transaction, String> column(
            String title, java.util.function.Function<Transaction, String> value) {
        TableColumn<Transaction, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(value.apply(cell.getValue())));
        return column;
    }

    private void saveTransaction() {
        try {
            Transaction transaction = selectedTransaction == null
                    ? formService.create(typeField.getValue(), amountField.getText(), dateText(),
                    categoryField.getValue(), paymentMethodField.getText(), notesField.getText())
                    : formService.update(selectedTransaction.id(), typeField.getValue(), amountField.getText(),
                    dateText(), categoryField.getValue(), paymentMethodField.getText(), notesField.getText());
            if (selectedTransaction == null) {
                store.add(transaction);
            } else {
                store.update(transaction);
            }
            clearForm();
            refreshHistory();
            budgetView.refresh();
            dashboardView.refresh();
            reportView.refresh();
            persist();
        } catch (RuntimeException exception) {
            showError(exception.getMessage());
        }
    }

    private void deleteSelected() {
        if (selectedTransaction != null) {
            store.delete(selectedTransaction.id());
            clearForm();
            refreshHistory();
            budgetView.refresh();
            dashboardView.refresh();
            reportView.refresh();
            persist();
        }
    }

    private void loadSelection(Transaction transaction) {
        selectedTransaction = transaction;
        if (transaction == null) {
            return;
        }
        typeField.setValue(transaction.type() == TransactionType.INCOME ? "Income" : "Expense");
        amountField.setText(transaction.amount().toPlainString());
        dateField.setValue(transaction.date());
        categoryField.setValue(transaction.category());
        paymentMethodField.setText(transaction.paymentMethod());
        notesField.setText(transaction.notes());
        saveButton.setText("Save changes");
    }

    private void clearForm() {
        selectedTransaction = null;
        typeField.setValue("Expense");
        amountField.clear();
        dateField.setValue(LocalDate.now());
        categoryField.setValue(categoryCatalog.find("Other"));
        paymentMethodField.clear();
        notesField.clear();
        historyTable.getSelectionModel().clearSelection();
        saveButton.setText("Add transaction");
    }

    private void refreshCategories() {
        List<Category> categories = categoryCatalog.all();
        categoryField.getItems().setAll(categories);
        categoryFilter.getItems().setAll(categories);
        categoryFilter.getItems().add(0, null);
        categoryFilter.setButtonCell(new javafx.scene.control.ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "All categories" : item.name());
            }
        });
        categoryFilter.setValue(null);
        categoryField.setValue(categoryCatalog.find("Other"));
    }

    private void refreshHistory() {
        TransactionType type = switch (typeFilter.getValue()) {
            case "Income" -> TransactionType.INCOME;
            case "Expense" -> TransactionType.EXPENSE;
            default -> null;
        };
        historyTable.setItems(FXCollections.observableArrayList(store.find(
                searchField.getText(), type, categoryFilter.getValue(), null, null)));
    }

    private void addCustomCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New category");
        dialog.setHeaderText("Create a custom expense category");
        dialog.setContentText("Name:");
        dialog.showAndWait().ifPresent(name -> {
            try {
                categoryCatalog.addCustom(name);
                refreshCategories();
                budgetView.updateCategories(categoryCatalog.all().toArray(Category[]::new));
                persist();
            } catch (RuntimeException exception) {
                showError(exception.getMessage());
            }
        });
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }

    private void persist() {
        try {
            persistenceService.save(dataDirectory, categoryCatalog.all(), store.all(), budgetStore.all());
        } catch (IllegalStateException exception) {
            showError("Unable to save data locally: " + exception.getMessage());
        }
    }

    private static javafx.util.StringConverter<Category> categoryConverter() {
        return new javafx.util.StringConverter<>() {
            @Override
            public String toString(Category category) {
                return category == null ? "" : category.name();
            }

            @Override
            public Category fromString(String value) {
                return null;
            }
        };
    }

    private String dateText() {
        return dateField.getValue() == null ? "" : dateField.getValue().toString();
    }
}
