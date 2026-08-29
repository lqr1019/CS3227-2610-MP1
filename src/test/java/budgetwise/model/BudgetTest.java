package budgetwise.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;

class BudgetTest {

    @Test
    void createsOverallAndCategoryBudgetScopes() {
        Budget overall = Budget.create(YearMonth.of(2026, 8), null, new BigDecimal("500"));
        Budget food = Budget.create(YearMonth.of(2026, 8), Category.builtIn("Food"), new BigDecimal("200"));

        assertEquals("All categories", overall.scopeName());
        assertEquals("Food", food.scopeName());
    }

    @Test
    void rejectsNonPositiveLimit() {
        assertThrows(IllegalArgumentException.class,
                () -> Budget.create(YearMonth.of(2026, 8), null, BigDecimal.ZERO));
    }
}
