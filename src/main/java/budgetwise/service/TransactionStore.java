package budgetwise.service;

import budgetwise.model.Category;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/** In-memory store providing transaction-history operations. */
public final class TransactionStore {

    private static final Comparator<Transaction> MOST_RECENT_FIRST =
            Comparator.comparing(Transaction::date).reversed()
                    .thenComparing(Transaction::id);

    private final List<Transaction> transactions = new ArrayList<>();

    /** Adds a transaction to the store. */
    public void add(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction");
        if (transactions.stream().anyMatch(existing -> existing.id().equals(transaction.id()))) {
            throw new IllegalArgumentException("A transaction with this id already exists");
        }
        transactions.add(transaction);
    }

    /** Replaces an existing transaction with the same id. */
    public void update(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction");
        int index = indexOf(transaction.id());
        if (index < 0) {
            throw new IllegalArgumentException("Transaction not found: " + transaction.id());
        }
        transactions.set(index, transaction);
    }

    /** Deletes a transaction, returning whether a record was removed. */
    public boolean delete(UUID id) {
        Objects.requireNonNull(id, "id");
        return transactions.removeIf(transaction -> transaction.id().equals(id));
    }

    /** Returns all transactions, ordered from most recent to oldest. */
    public List<Transaction> all() {
        return sortedCopy(transactions);
    }

    /** Finds transactions using optional type, category, date, and text filters. */
    public List<Transaction> find(
            String searchText,
            TransactionType type,
            Category category,
            LocalDate from,
            LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        String query = searchText == null ? "" : searchText.trim().toLowerCase(Locale.ROOT);
        return sortedCopy(transactions.stream()
                .filter(transaction -> type == null || transaction.type() == type)
                .filter(transaction -> category == null || transaction.category().equals(category))
                .filter(transaction -> from == null || !transaction.date().isBefore(from))
                .filter(transaction -> to == null || !transaction.date().isAfter(to))
                .filter(transaction -> matches(transaction, query))
                .toList());
    }

    private static boolean matches(Transaction transaction, String query) {
        if (query.isEmpty()) {
            return true;
        }
        return transaction.category().name().toLowerCase(Locale.ROOT).contains(query)
                || transaction.paymentMethod().toLowerCase(Locale.ROOT).contains(query)
                || transaction.notes().toLowerCase(Locale.ROOT).contains(query);
    }

    private int indexOf(UUID id) {
        for (int index = 0; index < transactions.size(); index++) {
            if (transactions.get(index).id().equals(id)) {
                return index;
            }
        }
        return -1;
    }

    private static List<Transaction> sortedCopy(List<Transaction> source) {
        return source.stream().sorted(MOST_RECENT_FIRST).toList();
    }
}
