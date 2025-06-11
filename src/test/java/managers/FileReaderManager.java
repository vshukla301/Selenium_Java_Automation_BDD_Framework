package managers;

import dataProviders.ConfigFileReader;

/**
 * FileReaderManager implements a Singleton pattern to manage the centralized access
 * to configuration file readers across the test framework. It ensures that only one
 * instance of ConfigFileReader is created and reused throughout the test lifecycle.
 *
 * This promotes consistency in accessing environment-specific configurations and avoids
 * unnecessary multiple initializations.
 */

public class FileReaderManager {
    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;

    private FileReaderManager() {
    }

    public static FileReaderManager getInstance() {
        return fileReaderManager;
    }

    public ConfigFileReader getConfigReader() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }
}
