package budgetwise.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void trimsNamesAndTracksWhetherCategoryIsCustom() {
        assertEquals("Food", new Category(" Food ", false).name());
        assertFalse(Category.builtIn("Food").custom());
        assertTrue(Category.custom("Travel").custom());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> Category.custom("  "));
    }
}
