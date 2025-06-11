package dataProviders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.EnvUtility;

import java.io.*;
import java.util.Properties;

/**
 * ConfigFileReader is a utility class designed to read environment-specific configuration
 * and email settings from .properties files. It supports dynamic environment detection (e.g., QA, Dev)
 * and provides convenient getter methods for accessing configuration values like URLs, credentials,
 * timeouts, and email credentials used in test automation.
 *
 * This promotes clean separation of config data from test logic and enables flexible, scalable automation.
 */

public class ConfigFileReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigFileReader.class);
    private Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config/";
    private static final String DEFAULT_ENV = "QA"; // Default to QA if not set

    public ConfigFileReader() {
        // Get environment from System Property (local) or Environment Variable (pipeline)
        String environment = EnvUtility.getTestEnvironment();
        System.out.println("*&*&*&*& " + environment);
        if (environment == null || environment.trim().isEmpty()) {
            environment = DEFAULT_ENV; // Default to QA if not provided
        }

        String propertyFilePath = CONFIG_PATH + "config_" + environment + ".properties";

        try (BufferedReader reader = new BufferedReader(new FileReader(propertyFilePath))) {
            properties = new Properties();
            properties.load(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Configuration file not found: " + propertyFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading configuration file: " + propertyFilePath);
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Property '" + key + "' not found in configuration file.");
        }
        return value;
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public long getLongProperty(String key) {
        return Long.parseLong(getProperty(key));
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }


    public String getUrl() {
        return getProperty("url");
    }

    public String getBrowser() {
        return getProperty("browser");
    }

    public String getValidUserId() {
        return getProperty("validUserId");
    }

    public String getValidPassword() {
        return getProperty("validPassword");
    }

    public String getInvalidUserId() {
        return getProperty("invalidUserId");
    }

    public String getInvalidPassword() {
        return getProperty("invalidPassword");
    }

    public int getPageLoadTimeout() { return Integer.parseInt(getProperty("pageLoadTimeout")); }

    public int getImplicitWait() { return Integer.parseInt(getProperty("implicitWait")); }

    public int getExplicitWait() { return Integer.parseInt(getProperty("explicitWait")); }


    private static Properties emailProperties = new Properties();

    // Load email configurations from a properties file
    static {
        try (InputStream input = new FileInputStream("src/test/resources/config/email.properties")) {
            emailProperties.load(input);
        } catch (Exception e) {
            logger.error("Failed to load email properties file", e);
        }
    }

    public String getEmailProperty(String key) {
        String value = emailProperties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Property '" + key + "' not found in configuration file.");
        }
        return value;
    }

    public String getEmailUserName() {
        return getEmailProperty("email.username");
    }

    public String getEmailPassword() {
        return getEmailProperty("email.password");
    }
}


