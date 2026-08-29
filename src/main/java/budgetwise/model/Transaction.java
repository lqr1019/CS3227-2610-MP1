package budgetwise.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/** An immutable income or expense record. */
public record Transaction(
        UUID id,
        TransactionType type,
        BigDecimal amount,
        LocalDate date,
        Category category,
        String paymentMethod,
        String notes) {

    /** Creates a validated transaction, trimming text fields. */
    public Transaction {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(category, "category");
        Objects.requireNonNull(paymentMethod, "paymentMethod");
        Objects.requireNonNull(notes, "notes");

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        paymentMethod = paymentMethod.trim();
        if (paymentMethod.isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be blank");
        }
        notes = notes.trim();
    }

    /** Creates a transaction with a newly generated identifier. */
    public static Transaction create(
            TransactionType type,
            BigDecimal amount,
            LocalDate date,
            Category category,
            String paymentMethod,
            String notes) {
        return new Transaction(UUID.randomUUID(), type, amount, date, category, paymentMethod, notes);
    }
}
