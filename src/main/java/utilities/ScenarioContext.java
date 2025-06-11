package utilities;

/**
 * ScenarioContext is a utility class used to maintain context information
 * during test execution, specifically the currently executing feature name.
 *
 * It uses a static variable to store the feature name globally across
 * the test run, allowing different components (e.g., logging, reporting)
 * to access or update this context as needed.
 *
 * Note:
 * - This class is not thread-safe. In parallel execution environments,
 *   consider using ThreadLocal to avoid context leakage across threads.
 */

public class ScenarioContext {

    private static String currentFeature;

    public static void setCurrentFeature(String featureName) {
        currentFeature = featureName;
    }

    public static String getCurrentFeature() {
        return currentFeature;
    }

}
