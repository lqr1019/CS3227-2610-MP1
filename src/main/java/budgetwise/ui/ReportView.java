package budgetwise.ui;

import budgetwise.model.Transaction;
import budgetwise.service.ReportCalculator;
import budgetwise.service.TransactionStore;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/** JavaFX report view with category, daily, weekly, and monthly charts. */
public final class ReportView extends BorderPane {

    private final TransactionStore transactionStore;
    private final ReportCalculator calculator = new ReportCalculator();
    private final TextField monthField = new TextField(YearMonth.now().toString());
    private final ComboBox<String> reportType = new ComboBox<>();
    private final BorderPane chartContainer = new BorderPane();

    /** Creates a report view backed by the supplied transaction store. */
    public ReportView(TransactionStore transactionStore) {
        this.transactionStore = transactionStore;
        reportType.getItems().setAll("By category", "By day", "By week", "By month");
        reportType.setValue("By category");
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refresh());
        reportType.setOnAction(event -> refresh());
        HBox controls = new HBox(8, new Label("Month (for category/day/week)"), monthField, reportType, refreshButton);
        controls.setPadding(new Insets(0, 0, 16, 0));
        setTop(controls);
        setCenter(chartContainer);
        refresh();
    }

    /** Rebuilds the selected report using current transactions. */
    public void refresh() {
        try {
            YearMonth month = YearMonth.parse(monthField.getText().trim());
            chartContainer.setCenter(switch (reportType.getValue()) {
                case "By day" -> lineChart("Daily expenses", calculator.byDay(month, transactionStore.all()));
                case "By week" -> barChart("Weekly expenses", calculator.byWeek(month, transactionStore.all()));
                case "By month" -> barChart("Monthly expenses", calculator.byMonth(transactionStore.all()));
                default -> pieChart(calculator.byCategory(month, transactionStore.all()));
            });
        } catch (RuntimeException exception) {
            chartContainer.setCenter(new Label("Enter a valid month using YYYY-MM"));
        }
    }

    private static PieChart pieChart(Map<String, BigDecimal> values) {
        PieChart chart = new PieChart();
        chart.setTitle("Expenses by category");
        values.forEach((label, amount) -> chart.getData().add(new PieChart.Data(label, amount.doubleValue())));
        return chart;
    }

    private static BarChart<String, Number> barChart(String title, Map<?, BigDecimal> values) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        values.forEach((label, amount) -> series.getData().add(new XYChart.Data<>(label.toString(), amount)));
        chart.getData().add(series);
        return chart;
    }

    private static LineChart<String, Number> lineChart(String title, Map<?, BigDecimal> values) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        values.forEach((label, amount) -> series.getData().add(new XYChart.Data<>(label.toString(), amount)));
        chart.getData().add(series);
        return chart;
    }
}
