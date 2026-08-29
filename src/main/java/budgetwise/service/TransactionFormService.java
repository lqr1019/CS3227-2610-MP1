package budgetwise.service;

import budgetwise.model.Category;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.UUID;

/** Converts user-entered form values into validated transactions. */
public final class TransactionFormService {

    /** Creates a transaction from form text. */
    public Transaction create(
            String type,
            String amount,
            String date,
            Category category,
            String paymentMethod,
            String notes) {
        return build(UUID.randomUUID(), type, amount, date, category, paymentMethod, notes);
    }

    /** Creates an edited transaction while preserving its identifier. */
    public Transaction update(
            UUID id,
            String type,
            String amount,
            String date,
            Category category,
            String paymentMethod,
            String notes) {
        return build(Objects.requireNonNull(id, "id"), type, amount, date, category, paymentMethod, notes);
    }

    private Transaction build(
            UUID id,
            String type,
            String amount,
            String date,
            Category category,
            String paymentMethod,
            String notes) {
        if (category == null) {
            throw new IllegalArgumentException("Category must be selected");
        }

        TransactionType parsedType;
        BigDecimal parsedAmount;
        LocalDate parsedDate;
        try {
            parsedType = TransactionType.valueOf(required(type, "Type").toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Type must be Income or Expense", exception);
        }
        try {
            parsedAmount = new BigDecimal(required(amount, "Amount"));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Amount must be a valid number", exception);
        }
        try {
            parsedDate = LocalDate.parse(required(date, "Date"));
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Date must use YYYY-MM-DD format", exception);
        }
        String parsedPaymentMethod = required(paymentMethod, "Payment method");
        return new Transaction(
                id, parsedType, parsedAmount, parsedDate, category, parsedPaymentMethod,
                notes == null ? "" : notes);
    }

    private static String required(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return value.trim();
    }
}
