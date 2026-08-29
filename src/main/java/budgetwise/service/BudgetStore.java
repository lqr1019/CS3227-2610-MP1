package budgetwise.service;

import budgetwise.model.Budget;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** In-memory store for budgets. */
public final class BudgetStore {

    private final List<Budget> budgets = new ArrayList<>();

    /** Adds a budget, rejecting a duplicate month and scope. */
    public void add(Budget budget) {
        Objects.requireNonNull(budget, "budget");
        boolean duplicate = budgets.stream().anyMatch(existing -> existing.month().equals(budget.month())
                && Objects.equals(existing.category(), budget.category()));
        if (duplicate) {
            throw new IllegalArgumentException("A budget already exists for this month and category");
        }
        budgets.add(budget);
    }

    /** Removes a budget by identifier. */
    public boolean delete(UUID id) {
        Objects.requireNonNull(id, "id");
        return budgets.removeIf(budget -> budget.id().equals(id));
    }

    /** Returns all budgets in month order. */
    public List<Budget> all() {
        return budgets.stream()
                .sorted((left, right) -> right.month().compareTo(left.month()))
                .toList();
    }
}
