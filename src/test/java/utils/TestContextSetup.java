package utils;

import managers.PageObjectManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
/**
 * TestContextSetup serves as a foundational context handler for each test execution.
 * It encapsulates the setup and state of the WebDriver and PageObjectManager,
 * enabling easy access to test utilities throughout the test lifecycle.
 *
 * Core Responsibilities:
 * - Initializes the WebDriver instance using the BaseTest class.
 * - Instantiates the PageObjectManager to provide access to page-level objects.
 * - Offers a method to open the application via URL and ensures full page load.
 *
 * Fields:
 * - driver: WebDriver instance shared across steps.
 * - pageObjectManager: Provides lazily-loaded page objects.
 * - baseTest: Used to initialize the browser and WebDriver with the necessary config.
 *
 * Constructor:
 * - Initializes WebDriver using configured browser settings.
 * - Creates a PageObjectManager instance tied to the current WebDriver.
 *
 * Method: openApplication(String url)
 * - Launches the application using the provided URL.
 * - Validates input to prevent null or empty URLs.
 * - Waits until the page is fully loaded using JavaScript's readyState check.
 */

public class TestContextSetup {
    public WebDriver driver;
    public PageObjectManager pageObjectManager;
    public BaseTest baseTest;
    private static final Logger logger = LoggerFactory.getLogger(TestContextSetup.class);
    public TestContextSetup() {
        baseTest = new BaseTest();
        driver = baseTest.initializeDriver();  // Ensures WebDriver is initialized
        pageObjectManager = new PageObjectManager(driver);  // Initializes PageObjectManager
    }

    /**
     * Opens the application using a given URL.
     * @param url The URL to open.
     */
    public void openApplication(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        driver.get(url);

        // Wait until the page loads completely
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }
}
