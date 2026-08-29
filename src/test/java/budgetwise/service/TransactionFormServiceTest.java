package budgetwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import budgetwise.model.Category;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TransactionFormServiceTest {

    private final TransactionFormService service = new TransactionFormService();
    private final Category food = Category.builtIn("Food");

    @Test
    void createsTransactionFromFormValues() {
        Transaction transaction = service.create("expense", " 12.50 ", "2026-08-29", food, " Card ", " Lunch ");

        assertEquals(TransactionType.EXPENSE, transaction.type());
        assertEquals("12.50", transaction.amount().toPlainString());
        assertEquals(LocalDate.of(2026, 8, 29), transaction.date());
        assertEquals("Card", transaction.paymentMethod());
        assertEquals("Lunch", transaction.notes());
    }

    @Test
    void updatePreservesIdentifier() {
        UUID id = UUID.randomUUID();

        assertEquals(id, service.update(id, "income", "100", "2026-08-01", food, "Bank", "Salary").id());
    }

    @Test
    void rejectsInvalidFormValues() {
        assertThrows(IllegalArgumentException.class,
                () -> service.create("expense", "not money", "2026-08-29", food, "Cash", ""));
        assertThrows(IllegalArgumentException.class,
                () -> service.create("expense", "10", "29-08-2026", food, "Cash", ""));
        assertThrows(IllegalArgumentException.class,
                () -> service.create("unknown", "10", "2026-08-29", food, "Cash", ""));
    }

    @Test
    void reportsEmptyPaymentMethodPrecisely() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.create("expense", "10", "2026-08-29", food, "  ", ""));

        assertEquals("Payment method cannot be empty", exception.getMessage());
    }

    @Test
    void reportsEmptyDatePrecisely() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.create("expense", "10", "  ", food, "Cash", ""));

        assertEquals("Date cannot be empty", exception.getMessage());
    }
}
