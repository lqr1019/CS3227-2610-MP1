package budgetwise.service;

import budgetwise.model.Budget;
import budgetwise.model.Category;
import budgetwise.model.Transaction;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Saves and loads BudgetWise data using Java's built-in object serialization. */
public final class PersistenceService {

    private static final String DATA_FILE = "budgetwise-data.ser";

    /** Container for all locally persisted application data. */
    public record SavedData(List<Category> categories, List<Transaction> transactions,
            List<Budget> budgets) implements java.io.Serializable {
        /** Returns an empty saved-data container. */
        public static SavedData empty() {
            return new SavedData(List.of(), List.of(), List.of());
        }
    }

    /** Loads data from the supplied directory, returning empty data if no file exists. */
    public SavedData load(Path dataDirectory) {
        Path dataFile = dataDirectory.resolve(DATA_FILE);
        if (!Files.exists(dataFile)) {
            return SavedData.empty();
        }
        try (InputStream input = Files.newInputStream(dataFile);
                ObjectInputStream objectInput = new ObjectInputStream(input)) {
            Object loaded = objectInput.readObject();
            if (!(loaded instanceof SavedData savedData)) {
                throw new IllegalStateException("Saved data has an invalid format");
            }
            return savedData;
        } catch (IOException | ClassNotFoundException exception) {
            throw new IllegalStateException("Unable to load saved BudgetWise data from " + dataFile, exception);
        }
    }

    /** Saves all application data under the supplied directory. */
    public void save(Path dataDirectory, List<Category> categories,
            List<Transaction> transactions, List<Budget> budgets) {
        try {
            Files.createDirectories(dataDirectory);
            Path dataFile = dataDirectory.resolve(DATA_FILE);
            try (OutputStream output = Files.newOutputStream(dataFile);
                    ObjectOutputStream objectOutput = new ObjectOutputStream(output)) {
                objectOutput.writeObject(new SavedData(List.copyOf(categories),
                        List.copyOf(transactions), List.copyOf(budgets)));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save BudgetWise data", exception);
        }
    }
}
