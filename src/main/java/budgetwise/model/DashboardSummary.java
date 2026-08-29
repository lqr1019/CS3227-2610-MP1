package budgetwise.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/** Calculated financial summary for one calendar month. */
public record DashboardSummary(BigDecimal totalIncome, BigDecimal totalExpenses,
        Map<LocalDate, BigDecimal> dailyExpenses) {

    /** Returns income minus expenses. */
    public BigDecimal balance() {
        return totalIncome.subtract(totalExpenses);
    }
}
