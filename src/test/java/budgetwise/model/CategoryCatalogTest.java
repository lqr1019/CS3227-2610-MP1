package budgetwise.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CategoryCatalogTest {

    @Test
    void startsWithStandardCategories() {
        CategoryCatalog catalog = new CategoryCatalog();

        assertEquals(9, catalog.all().size());
        assertEquals("Food", catalog.find(" food ").name());
        assertFalse(catalog.find("Food").custom());
    }

    @Test
    void addsCustomCategoryAndDoesNotDuplicateIgnoringCase() {
        CategoryCatalog catalog = new CategoryCatalog();

        Category added = catalog.addCustom("  Travel  ");
        Category duplicate = catalog.addCustom("travel");

        assertEquals("Travel", added.name());
        assertSame(added, duplicate);
        assertEquals(10, catalog.all().size());
    }

    @Test
    void rejectsUnknownCategoryLookup() {
        assertThrows(IllegalArgumentException.class, () -> new CategoryCatalog().find("Unknown"));
    }
}
