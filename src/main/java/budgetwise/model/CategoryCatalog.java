package budgetwise.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/** In-memory catalog of built-in and user-defined transaction categories. */
public final class CategoryCatalog {

    private static final List<String> DEFAULT_NAMES = List.of(
            "Food",
            "Transport",
            "Shopping",
            "Entertainment",
            "Rent",
            "Bills",
            "Education",
            "Salary",
            "Other");

    private final Set<Category> categories = new LinkedHashSet<>();

    /** Creates a catalog populated with the standard categories. */
    public CategoryCatalog() {
        DEFAULT_NAMES.stream().map(Category::builtIn).forEach(categories::add);
    }

    /** Returns all categories in display order. */
    public List<Category> all() {
        return List.copyOf(new ArrayList<>(categories));
    }

    /** Adds a custom category unless a category with the same name exists. */
    public Category addCustom(String name) {
        Category category = Category.custom(name);
        boolean duplicate = categories.stream()
                .anyMatch(existing -> existing.name().toLowerCase(Locale.ROOT)
                        .equals(category.name().toLowerCase(Locale.ROOT)));
        if (!duplicate) {
            categories.add(category);
            return category;
        }
        return categories.stream()
                .filter(existing -> existing.name().equalsIgnoreCase(category.name()))
                .findFirst()
                .orElseThrow();
    }

    /** Finds a category by name, ignoring case and surrounding whitespace. */
    public Category find(String name) {
        Objects.requireNonNull(name, "name");
        String normalizedName = name.trim();
        return categories.stream()
                .filter(category -> category.name().equalsIgnoreCase(normalizedName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + name));
    }
}
