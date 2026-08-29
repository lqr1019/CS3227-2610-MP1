package budgetwise.service;

import budgetwise.model.DashboardSummary;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;

/** Calculates monthly dashboard values from transactions. */
public final class DashboardCalculator {

    /** Calculates income, expenses, balance data, and daily expense totals. */
    public DashboardSummary summarize(YearMonth month, List<Transaction> transactions) {
        Objects.requireNonNull(month, "month");
        Objects.requireNonNull(transactions, "transactions");
        Predicate<Transaction> inMonth = transaction -> month.equals(YearMonth.from(transaction.date()));
        BigDecimal income = total(transactions, inMonth.and(t -> t.type() == TransactionType.INCOME));
        BigDecimal expenses = total(transactions, inMonth.and(t -> t.type() == TransactionType.EXPENSE));
        Map<LocalDate, BigDecimal> dailyExpenses = new TreeMap<>();
        transactions.stream().filter(inMonth).filter(t -> t.type() == TransactionType.EXPENSE)
                .forEach(t -> dailyExpenses.merge(t.date(), t.amount(), BigDecimal::add));
        return new DashboardSummary(income, expenses, Collections.unmodifiableMap(dailyExpenses));
    }

    private static BigDecimal total(List<Transaction> transactions, Predicate<Transaction> condition) {
        return transactions.stream().filter(condition).map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
