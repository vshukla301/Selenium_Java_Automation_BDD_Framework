package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.mail.Store;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 📧 EmailConnector is a utility class responsible for establishing a secure IMAPS connection
 * to a Gmail mailbox using configuration loaded from a properties file.
 *
 * 🔐 Uses SSL-enabled IMAPS protocol for secure access.
 * 🛠 Loads host and port from 'email.properties' allowing dynamic configuration.
 * ⏳ Supports optional delay to accommodate scenarios where the email may take time to arrive.
 * 🧾 Returns a connected javax.mail.Store instance for downstream operations like inbox scanning.
 *
 * Example properties expected in 'src/test/resources/config/email.properties':
 *  - imap.host=imap.gmail.com
 *  - imap.port=993
 *
 * Logging is integrated using SLF4J to aid in connection diagnostics.
 */
public class EmailConnector {
    private static final Logger logger = LoggerFactory.getLogger(EmailConnector.class);
    private static Properties emailProperties = new Properties();

    // ⚙️ Static block to load email configuration only once
    static {
        try (InputStream input = new FileInputStream("src/test/resources/config/email.properties")) {
            emailProperties.load(input);
        } catch (Exception e) {
            logger.error("❌ Failed to load email properties file", e);
        }
    }

    /**
     * Establishes a secure connection to Gmail using IMAPS protocol.
     *
     * @param username     Gmail account username (email address)
     * @param password     Gmail account password or App password
     * @param timeToWait   Delay in minutes before initiating the connection
     * @return Store       Connected mail store instance for folder access
     * @throws Exception   If connection fails or is interrupted
     */
    public static Store connectToGmail(String username, String password, int timeToWait) throws Exception {
        try {
            Thread.sleep(timeToWait * 60 * 1000); // ⏳ Wait for specified duration
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 🛠 Prepare email session properties
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imap.host", emailProperties.getProperty("imap.host", "imap.gmail.com"));
        props.put("mail.imap.port", emailProperties.getProperty("imap.port", "993"));
        props.put("mail.imap.ssl.enable", "true");

        Session session = Session.getInstance(props);
        Store store = session.getStore("imaps");

        logger.info("📡 Connecting to Gmail...");
        store.connect(props.getProperty("mail.imap.host"), username, password);
        logger.info("✅ Connection established successfully.");
        return store;
    }
}
