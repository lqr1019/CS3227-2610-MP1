package budgetwise.model;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

/** A spending limit for one month, optionally restricted to a category. */
public record Budget(UUID id, YearMonth month, Category category, BigDecimal limit) {

    /** Creates a validated budget. A null category represents an overall budget. */
    public Budget {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(month, "month");
        Objects.requireNonNull(limit, "limit");
        if (limit.signum() <= 0) {
            throw new IllegalArgumentException("Budget limit must be greater than zero");
        }
    }

    /** Creates a budget with a newly generated identifier. */
    public static Budget create(YearMonth month, Category category, BigDecimal limit) {
        return new Budget(UUID.randomUUID(), month, category, limit);
    }

    /** Returns a label suitable for displaying the budget scope. */
    public String scopeName() {
        return category == null ? "All categories" : category.name();
    }
}
