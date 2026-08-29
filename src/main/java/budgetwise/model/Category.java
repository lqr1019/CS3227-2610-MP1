package budgetwise.model;

import java.util.Objects;

/** A label used to classify a transaction. */
public record Category(String name, boolean custom) {

    /** Creates a category with a non-blank, trimmed name. */
    public Category {
        Objects.requireNonNull(name, "name");
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be blank");
        }
    }

    /** Creates a built-in category. */
    public static Category builtIn(String name) {
        return new Category(name, false);
    }

    /** Creates a user-defined category. */
    public static Category custom(String name) {
        return new Category(name, true);
    }

    /** Returns the display name used by user-interface controls. */
    @Override
    public String toString() {
        return name;
    }
}
