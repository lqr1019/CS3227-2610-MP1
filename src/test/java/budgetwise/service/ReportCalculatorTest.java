package budgetwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import budgetwise.model.Category;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReportCalculatorTest {

    private final ReportCalculator calculator = new ReportCalculator();
    private final List<Transaction> transactions = List.of(
            transaction(TransactionType.EXPENSE, "Food", "10", "2026-08-03"),
            transaction(TransactionType.EXPENSE, "Food", "15", "2026-08-03"),
            transaction(TransactionType.EXPENSE, "Transport", "20", "2026-08-10"),
            transaction(TransactionType.EXPENSE, "Food", "30", "2026-09-01"),
            transaction(TransactionType.INCOME, "Food", "100", "2026-08-03"));

    @Test
    void aggregatesExpensesByCategoryAndDay() {
        assertEquals(Map.of("Food", new BigDecimal("25"), "Transport", new BigDecimal("20")),
                calculator.byCategory(YearMonth.of(2026, 8), transactions));
        assertEquals(Map.of(LocalDate.of(2026, 8, 3), new BigDecimal("25"),
                LocalDate.of(2026, 8, 10), new BigDecimal("20")),
                calculator.byDay(YearMonth.of(2026, 8), transactions));
    }

    @Test
    void aggregatesExpensesByWeekAndMonth() {
        assertEquals(new BigDecimal("45"), calculator.byWeek(YearMonth.of(2026, 8), transactions)
                .values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
        assertEquals(Map.of(YearMonth.of(2026, 8), new BigDecimal("45"),
                YearMonth.of(2026, 9), new BigDecimal("30")), calculator.byMonth(transactions));
    }

    private static Transaction transaction(TransactionType type, String category, String amount, String date) {
        return new Transaction(UUID.randomUUID(), type, new BigDecimal(amount), LocalDate.parse(date),
                Category.builtIn(category), "Card", "");
    }
}
