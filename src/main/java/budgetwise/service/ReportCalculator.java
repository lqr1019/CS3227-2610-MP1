package budgetwise.service;

import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

/** Aggregates expense transactions for charts and reports. */
public final class ReportCalculator {

    /** Returns expenses grouped by category for a month. */
    public Map<String, BigDecimal> byCategory(YearMonth month, List<Transaction> transactions) {
        return aggregate(month, transactions, transaction -> transaction.category().name());
    }

    /** Returns expenses grouped by day for a month. */
    public Map<LocalDate, BigDecimal> byDay(YearMonth month, List<Transaction> transactions) {
        return aggregate(month, transactions, Transaction::date);
    }

    /** Returns expenses grouped by ISO week for a month. */
    public Map<String, BigDecimal> byWeek(YearMonth month, List<Transaction> transactions) {
        WeekFields weekFields = WeekFields.ISO;
        return aggregate(month, transactions, transaction -> {
            int week = transaction.date().get(weekFields.weekOfWeekBasedYear());
            int year = transaction.date().get(weekFields.weekBasedYear());
            return String.format("%d-W%02d", year, week);
        });
    }

    /** Returns expenses grouped by calendar month across all supplied transactions. */
    public Map<YearMonth, BigDecimal> byMonth(List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "transactions");
        return transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .collect(TreeMap::new,
                        (result, transaction) -> result.merge(
                                YearMonth.from(transaction.date()), transaction.amount(), BigDecimal::add),
                        TreeMap::putAll);
    }

    private static <K> Map<K, BigDecimal> aggregate(
            YearMonth month, List<Transaction> transactions, Function<Transaction, K> keyFunction) {
        Objects.requireNonNull(month, "month");
        Objects.requireNonNull(transactions, "transactions");
        return transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .filter(transaction -> month.equals(YearMonth.from(transaction.date())))
                .collect(TreeMap::new,
                        (result, transaction) -> result.merge(keyFunction.apply(transaction), transaction.amount(), BigDecimal::add),
                        TreeMap::putAll);
    }
}
