package budgetwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import budgetwise.model.Budget;
import budgetwise.model.Category;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BudgetCalculatorTest {

    private static final Category FOOD = Category.builtIn("Food");
    private static final Category TRANSPORT = Category.builtIn("Transport");
    private final BudgetCalculator calculator = new BudgetCalculator();

    @Test
    void calculatesMonthlyOverallSpendingAndRemaining() {
        Budget budget = Budget.create(YearMonth.of(2026, 8), null, new BigDecimal("100"));
        List<Transaction> transactions = List.of(
                transaction(TransactionType.EXPENSE, "20", LocalDate.of(2026, 8, 2), FOOD),
                transaction(TransactionType.EXPENSE, "15", LocalDate.of(2026, 8, 30), TRANSPORT),
                transaction(TransactionType.EXPENSE, "90", LocalDate.of(2026, 9, 1), FOOD),
                transaction(TransactionType.INCOME, "500", LocalDate.of(2026, 8, 2), FOOD));

        assertEquals(new BigDecimal("35"), calculator.spent(budget, transactions));
        assertEquals(new BigDecimal("65"), calculator.remaining(budget, transactions));
    }

    @Test
    void calculatesCategorySpendingAndSupportsOverspending() {
        Budget budget = Budget.create(YearMonth.of(2026, 8), FOOD, new BigDecimal("25"));
        List<Transaction> transactions = List.of(
                transaction(TransactionType.EXPENSE, "30", LocalDate.of(2026, 8, 2), FOOD),
                transaction(TransactionType.EXPENSE, "10", LocalDate.of(2026, 8, 2), TRANSPORT));

        assertEquals(new BigDecimal("30"), calculator.spent(budget, transactions));
        assertEquals(new BigDecimal("-5"), calculator.remaining(budget, transactions));
    }

    private static Transaction transaction(
            TransactionType type, String amount, LocalDate date, Category category) {
        return new Transaction(UUID.randomUUID(), type, new BigDecimal(amount), date, category, "Card", "");
    }
}
