package utils;

import managers.FileReaderManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseTest class handles the initialization and configuration of WebDriver instances.
 * It supports multiple browser types and sets timeout values and default behaviors
 * required for consistent browser session management.
 */
public class BaseTest {
    public WebDriver driver; // Instance of WebDriver for test execution
    public List<Dimension> screenDimensionsList; // Used to emulate mobile device resolutions
    public long implicitWait; // Timeout for implicit wait
    public long pageLoadTimeout; // Timeout for page load

    /**
     * Initializes WebDriver based on the browser name configured in the properties file.
     * Supports Chrome, Firefox, Edge, Headless Chrome, and a mobile emulation (iPhone).
     * Sets all browser session configurations including timeouts and window size.
     *
     * @return WebDriver instance ready for use in test execution.
     */
    public WebDriver initializeDriver() {
        if (driver == null) {
            String browser = FileReaderManager.getInstance().getConfigReader().getBrowser();

            if (browser.equalsIgnoreCase("Chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setAcceptInsecureCerts(true);
                driver = new ChromeDriver(options);
            } else if (browser.equalsIgnoreCase("Firefox")) {
                driver = new FirefoxDriver();
            } else if (browser.equalsIgnoreCase("Edge")) {
                driver = new EdgeDriver();
            } else if (browser.equalsIgnoreCase("Headless")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
                options.setAcceptInsecureCerts(true);
                driver = new ChromeDriver(options);
            } else if (browser.equalsIgnoreCase("Iphone")) {
                screenDimensionsList = new ArrayList<>();
                screenDimensionsList.add(new Dimension(375, 667)); // Emulates iPhone screen resolution
                driver = new ChromeDriver();
                for (Dimension d : screenDimensionsList) {
                    driver.manage().window().setSize(d);
                }
            }

            // Common browser session setup
            driver.manage().window().maximize();
            driver.get(FileReaderManager.getInstance().getConfigReader().getUrl());
            implicitWait = FileReaderManager.getInstance().getConfigReader().getImplicitWait();
            pageLoadTimeout = FileReaderManager.getInstance().getConfigReader().getPageLoadTimeout();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            driver.manage().deleteAllCookies();
        }

        return driver;
    }
}
