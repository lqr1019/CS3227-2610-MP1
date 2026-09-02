package budgetwise;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/** Platform-selecting bootstrap entry point for executable JAR launches. */
public final class Launcher {

    private static final String JAVAFX_DIRECTORY = "javafx-libs/";

    private Launcher() {
        // Utility class.
    }

    /** Launches the JavaFX application. */
    public static void main(String[] args) {
        try {
            Path applicationJar = Path.of(Launcher.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            if (Files.isDirectory(applicationJar)) {
                JavaFxLauncher.main(args);
                return;
            }
            Path extractedLibraries = extractPlatformLibraries(applicationJar);
            List<URL> classpath = new ArrayList<>();
            classpath.add(applicationJar.toUri().toURL());
            try (JarFile jarFile = new JarFile(applicationJar.toFile())) {
                jarFile.stream()
                        .filter(entry -> entry.getName().startsWith(JAVAFX_DIRECTORY))
                        .filter(entry -> entry.getName().endsWith(".jar"))
                        .filter(entry -> entry.getName().contains("-" + platformClassifier() + ".jar"))
                        .map(entry -> extractedLibraries.resolve(Path.of(entry.getName()).getFileName()))
                        .map(Launcher::toUrl)
                        .forEach(classpath::add);
            }
            try (URLClassLoader classLoader = new URLClassLoader(
                    classpath.toArray(URL[]::new), Launcher.class.getClassLoader())) {
                Thread.currentThread().setContextClassLoader(classLoader);
                Class<?> entryPoint = Class.forName("budgetwise.JavaFxLauncher", true, classLoader);
                entryPoint.getMethod("main", String[].class).invoke(null, (Object) args);
            }
        } catch (IOException | ReflectiveOperationException | java.net.URISyntaxException exception) {
            throw new IllegalStateException("Unable to start BudgetWise with the platform JavaFX runtime", exception);
        }
    }

    private static Path extractPlatformLibraries(Path applicationJar) throws IOException {
        Path directory = Files.createTempDirectory("budgetwise-javafx-");
        try (JarFile jarFile = new JarFile(applicationJar.toFile())) {
            jarFile.stream()
                    .filter(entry -> entry.getName().startsWith(JAVAFX_DIRECTORY))
                    .filter(entry -> entry.getName().endsWith(".jar"))
                    .filter(entry -> entry.getName().contains("-" + platformClassifier() + ".jar"))
                    .forEach(entry -> extract(jarFile, entry, directory));
        }
        return directory;
    }

    private static void extract(JarFile jarFile, JarEntry entry, Path directory) {
        Path target = directory.resolve(Path.of(entry.getName()).getFileName());
        try (InputStream input = jarFile.getInputStream(entry)) {
            Files.copy(input, target);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to extract JavaFX runtime", exception);
        }
    }

    private static URL toUrl(Path path) {
        try {
            return path.toUri().toURL();
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static String platformClassifier() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String architecture = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        String operatingSystem = os.contains("win") ? "win" : os.contains("mac") ? "mac" : "linux";
        boolean arm = architecture.contains("aarch64") || architecture.contains("arm64");
        return arm ? operatingSystem + "-aarch64" : operatingSystem;
    }
}
