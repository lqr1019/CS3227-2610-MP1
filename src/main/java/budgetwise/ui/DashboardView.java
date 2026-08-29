package budgetwise.ui;

import budgetwise.model.DashboardSummary;
import budgetwise.service.DashboardCalculator;
import budgetwise.service.TransactionStore;
import java.math.BigDecimal;
import java.time.YearMonth;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** JavaFX monthly dashboard showing totals and daily expense trends. */
@SuppressWarnings("unchecked")
public final class DashboardView extends BorderPane {

    private final TransactionStore transactionStore;
    private final DashboardCalculator calculator = new DashboardCalculator();
    private final TextField monthField = new TextField(YearMonth.now().toString());
    private final Label incomeValue = new Label();
    private final Label expensesValue = new Label();
    private final Label balanceValue = new Label();
    private final LineChart<String, Number> expenseChart;

    /** Creates a dashboard backed by the supplied transaction store. */
    public DashboardView(TransactionStore transactionStore) {
        this.transactionStore = transactionStore;
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day");
        yAxis.setLabel("Expenses");
        expenseChart = new LineChart<>(xAxis, yAxis);
        expenseChart.setTitle("Daily expenses");
        expenseChart.setLegendVisible(false);
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refresh());
        HBox controls = new HBox(8, new Label("Month (YYYY-MM)"), monthField, refreshButton);
        controls.setPadding(new Insets(0, 0, 16, 0));
        GridPane totals = new GridPane();
        totals.setHgap(30);
        totals.setVgap(6);
        totals.addRow(0, new Label("Total income"), incomeValue,
                new Label("Total expenses"), expensesValue,
                new Label("Balance"), balanceValue);
        VBox content = new VBox(16, controls, totals, expenseChart);
        VBox.setVgrow(expenseChart, javafx.scene.layout.Priority.ALWAYS);
        setCenter(content);
        refresh();
    }

    /** Recalculates dashboard values for the selected month. */
    public void refresh() {
        try {
            DashboardSummary summary = calculator.summarize(
                    YearMonth.parse(monthField.getText().trim()), transactionStore.all());
            incomeValue.setText(format(summary.totalIncome()));
            expensesValue.setText(format(summary.totalExpenses()));
            balanceValue.setText(format(summary.balance()));
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            summary.dailyExpenses().forEach((date, amount) ->
                    series.getData().add(new XYChart.Data<>(String.valueOf(date.getDayOfMonth()), amount)));
            expenseChart.getData().setAll(series);
        } catch (RuntimeException exception) {
            incomeValue.setText("Invalid month");
            expensesValue.setText("Invalid month");
            balanceValue.setText("Invalid month");
            expenseChart.getData().clear();
        }
    }

    private static String format(BigDecimal value) {
        return value.toPlainString();
    }
}
