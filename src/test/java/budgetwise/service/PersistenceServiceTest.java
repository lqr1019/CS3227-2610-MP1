package budgetwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import budgetwise.model.Budget;
import budgetwise.model.Category;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PersistenceServiceTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void savedDataCanBeLoadedAfterTheOriginalServiceIsRecreated() {
        PersistenceService firstService = new PersistenceService();
        Category food = Category.custom("Meal prep");
        Transaction transaction = new Transaction(UUID.randomUUID(), TransactionType.EXPENSE,
                new BigDecimal("12.50"), LocalDate.of(2026, 8, 29), food, "Card", "Lunch");
        Budget budget = Budget.create(YearMonth.of(2026, 8), food, new BigDecimal("100"));

        firstService.save(temporaryDirectory, List.of(food), List.of(transaction), List.of(budget));
        PersistenceService.SavedData loaded = new PersistenceService().load(temporaryDirectory);

        assertEquals(List.of(food), loaded.categories());
        assertEquals(List.of(transaction), loaded.transactions());
        assertEquals(List.of(budget), loaded.budgets());
    }
}
