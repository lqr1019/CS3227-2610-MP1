package budgetwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import budgetwise.model.Category;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionStoreTest {

    private static final Category FOOD = Category.builtIn("Food");
    private static final Category TRANSPORT = Category.builtIn("Transport");

    private TransactionStore store;
    private Transaction lunch;
    private Transaction salary;

    @BeforeEach
    void setUp() {
        store = new TransactionStore();
        lunch = transaction(TransactionType.EXPENSE, "Food", LocalDate.of(2026, 8, 20), "Lunch");
        salary = transaction(TransactionType.INCOME, "Salary", LocalDate.of(2026, 8, 1), "August salary");
        store.add(lunch);
        store.add(salary);
    }

    @Test
    void returnsTransactionsMostRecentFirst() {
        assertEquals(List.of(lunch, salary), store.all());
    }

    @Test
    void updatesAndDeletesByIdentifier() {
        Transaction updated = new Transaction(
                lunch.id(), TransactionType.EXPENSE, new BigDecimal("15.00"), lunch.date(),
                TRANSPORT, "Card", "Taxi");

        store.update(updated);
        assertEquals(updated, store.all().get(0));
        assertTrue(store.delete(updated.id()));
        assertEquals(1, store.all().size());
        assertFalse(store.delete(updated.id()));
    }

    @Test
    void searchesAcrossCategoryPaymentMethodAndNotes() {
        assertEquals(List.of(lunch), store.find("lunch", null, null, null, null));
        assertEquals(List.of(salary), store.find("salary", null, null, null, null));
        assertEquals(List.of(lunch), store.find("food", null, null, null, null));
    }

    @Test
    void filtersByTypeCategoryAndInclusiveDateRange() {
        assertEquals(List.of(lunch), store.find(null, TransactionType.EXPENSE, FOOD,
                LocalDate.of(2026, 8, 20), LocalDate.of(2026, 8, 20)));
    }

    @Test
    void rejectsInvalidDateRangeAndUnknownUpdates() {
        assertThrows(IllegalArgumentException.class, () -> store.find(
                null, null, null, LocalDate.of(2026, 8, 21), LocalDate.of(2026, 8, 20)));
        Transaction unknown = transaction(TransactionType.EXPENSE, "Food", LocalDate.now(), "Unknown");
        assertThrows(IllegalArgumentException.class, () -> store.update(unknown));
    }

    @Test
    void rejectsDuplicateIdentifiers() {
        assertThrows(IllegalArgumentException.class, () -> store.add(lunch));
    }

    private static Transaction transaction(
            TransactionType type, String categoryName, LocalDate date, String notes) {
        Category category = categoryName.equals("Food") ? FOOD : Category.builtIn(categoryName);
        return new Transaction(UUID.randomUUID(), type, new BigDecimal("10.00"), date,
                category, "Card", notes);
    }
}
