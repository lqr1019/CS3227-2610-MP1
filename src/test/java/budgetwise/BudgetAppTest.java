package budgetwise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests the non-UI contract of the initial application release. */
class BudgetAppTest {

    @Test
    void applicationClassHasExpectedName() {
        assertEquals("BudgetApp", BudgetApp.class.getSimpleName());
    }
}
