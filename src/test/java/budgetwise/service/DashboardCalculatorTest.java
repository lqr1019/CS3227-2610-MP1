package budgetwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import budgetwise.model.Category;
import budgetwise.model.DashboardSummary;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DashboardCalculatorTest {

    private final DashboardCalculator calculator = new DashboardCalculator();

    @Test
    void summarizesMonthlyIncomeExpensesBalanceAndDailyExpenses() {
        List<Transaction> transactions = List.of(
                transaction(TransactionType.INCOME, "1000", "2026-08-01"),
                transaction(TransactionType.EXPENSE, "20", "2026-08-02"),
                transaction(TransactionType.EXPENSE, "15", "2026-08-02"),
                transaction(TransactionType.EXPENSE, "90", "2026-09-01"));

        DashboardSummary summary = calculator.summarize(YearMonth.of(2026, 8), transactions);

        assertEquals(new BigDecimal("1000"), summary.totalIncome());
        assertEquals(new BigDecimal("35"), summary.totalExpenses());
        assertEquals(new BigDecimal("965"), summary.balance());
        assertEquals(new BigDecimal("35"), summary.dailyExpenses().get(LocalDate.of(2026, 8, 2)));
    }

    @Test
    void returnsZeroValuesForMonthWithoutTransactions() {
        DashboardSummary summary = calculator.summarize(YearMonth.of(2026, 7), List.of());

        assertEquals(BigDecimal.ZERO, summary.totalIncome());
        assertEquals(BigDecimal.ZERO, summary.totalExpenses());
        assertEquals(BigDecimal.ZERO, summary.balance());
        assertEquals(0, summary.dailyExpenses().size());
    }

    private static Transaction transaction(TransactionType type, String amount, String date) {
        return new Transaction(UUID.randomUUID(), type, new BigDecimal(amount), LocalDate.parse(date),
                Category.builtIn("Other"), "Card", "");
    }
}
