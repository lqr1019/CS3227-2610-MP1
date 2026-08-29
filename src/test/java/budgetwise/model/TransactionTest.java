package budgetwise.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class TransactionTest {

    private static final Category FOOD = Category.builtIn("Food");

    @Test
    void createsTransactionWithGeneratedIdentifierAndTrimmedText() {
        Transaction transaction = Transaction.create(
                TransactionType.EXPENSE,
                new BigDecimal("12.50"),
                LocalDate.of(2026, 8, 29),
                FOOD,
                " Card ",
                " Lunch ");

        assertNotNull(transaction.id());
        assertEquals(TransactionType.EXPENSE, transaction.type());
        assertEquals("Card", transaction.paymentMethod());
        assertEquals("Lunch", transaction.notes());
    }

    @Test
    void preservesSuppliedIdentifierAndAmount() {
        UUID id = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");

        Transaction transaction = new Transaction(
                id, TransactionType.INCOME, amount, LocalDate.now(), FOOD, "Bank transfer", "Salary");

        assertEquals(id, transaction.id());
        assertEquals(amount, transaction.amount());
    }

    @Test
    void rejectsNonPositiveAmount() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                UUID.randomUUID(), TransactionType.EXPENSE, BigDecimal.ZERO,
                LocalDate.now(), FOOD, "Cash", ""));
    }

    @Test
    void rejectsBlankPaymentMethod() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                UUID.randomUUID(), TransactionType.EXPENSE, BigDecimal.ONE,
                LocalDate.now(), FOOD, "  ", ""));
    }
}
