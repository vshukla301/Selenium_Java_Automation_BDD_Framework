package utilities;

import java.util.Optional;

/**
 * EnvUtility is a helper class designed to manage and interpret environment-level
 * configurations such as test environment names and Cucumber tags.
 *
 * It pulls values dynamically from:
 *   - Java system properties (`System.getProperty`)
 *   - Environment variables (`System.getenv`)
 *
 * Primary Use Cases:
 *   - Determine the current environment: QA, PROD, etc.
 *   - Extract and format execution-level Cucumber tags
 *   - Format report subject lines with dynamic values
 *
 * Fallback logic ensures smooth retrieval even if properties are unset.
 */
public class EnvUtility {

    // Reads the test environment from system property or environment variable
    private static final String TEST_ENV = Optional.ofNullable(System.getProperty("TEST_ENV"))
            .orElse(System.getenv("TEST_ENV"));

    // Reads the Cucumber tag from system property or environment variable
    private static final String CUCUMBER_TAG = Optional.ofNullable(System.getProperty("CUCUMBER_TAG"))
            .orElse(System.getenv("CUCUMBER_TAG"));

    /**
     * Retrieves the configured test environment.
     * @return the environment name as a String (e.g., QA, PROD)
     */
    public static String getTestEnvironment() {
        return TEST_ENV;
    }

    /**
     * Retrieves the Cucumber tag for test execution.
     * Removes the "@" prefix if present and converts it to uppercase.
     * @return the formatted tag (e.g., "SMOKE", "SANITY")
     */
    public static String getCucumberTag() {
        return Optional.ofNullable(CUCUMBER_TAG)
                .map(tag -> tag.replace("@", "").toUpperCase())
                .orElse("SANITY");
    }

    /**
     * Checks if the current environment is 'PROD'
     * @return true if in PROD environment
     */
    public static boolean isProd() {
        return "PROD".equalsIgnoreCase(TEST_ENV);
    }

    /**
     * Checks if the current environment is 'QA'
     * @return true if in QA environment
     */
    public static boolean isQA() {
        return "QA".equalsIgnoreCase(TEST_ENV);
    }

    /**
     * Constructs a subject line for automated test report emails.
     * Format: Automation Test Report [ENV] - TAG
     * @return a formatted subject string
     */
    public static String getReportSubject() {
        return String.format("Automation Test Report [%s] - %s", TEST_ENV, CUCUMBER_TAG);
    }
}
