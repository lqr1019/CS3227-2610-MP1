package budgetwise.service;

import budgetwise.model.Budget;
import budgetwise.model.Transaction;
import budgetwise.model.TransactionType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/** Calculates budget usage from in-memory transactions. */
public final class BudgetCalculator {

    /** Returns total matching expenses for a budget. */
    public BigDecimal spent(Budget budget, List<Transaction> transactions) {
        Objects.requireNonNull(budget, "budget");
        Objects.requireNonNull(transactions, "transactions");
        return transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .filter(transaction -> budget.month().equals(java.time.YearMonth.from(transaction.date())))
                .filter(transaction -> budget.category() == null
                        || budget.category().equals(transaction.category()))
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Returns the budget limit minus matching expenses. Negative means overspent. */
    public BigDecimal remaining(Budget budget, List<Transaction> transactions) {
        return budget.limit().subtract(spent(budget, transactions));
    }
}
