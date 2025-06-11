package utils;

import enums.EmailConstants;
import managers.FileReaderManager;
import stepDefinitions.Hooks;
import utilities.EnvUtility;
import utilities.ReportParser;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Utility class responsible for constructing and sending an HTML email
 * with the automation execution summary and the latest test report as an attachment.
 */
public class EmailUtil {

    /**
     * Constructs a summary email with execution statistics and sends it
     * to a predefined list of recipients.
     *
     * Key features:
     * - Collects execution counts and statuses from the Hooks class.
     * - Builds a styled HTML message body including emojis and scenario details.
     * - Attaches the latest generated report file.
     * - Authenticates and sends the email using Gmail SMTP.
     */
    public static void sendEmailWithReport() {

        // Load credentials and environment info
        final String username = FileReaderManager.getInstance().getConfigReader().getEmailUserName();
        final String password = FileReaderManager.getInstance().getConfigReader().getEmailPassword();
        final String environment = EnvUtility.getTestEnvironment();
        final String tag = EnvUtility.getCucumberTag();

        // Define recipient list
        String[] recipients = EmailConstants.EmailRecipientsList;

        // Setup SMTP properties
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");

        // Prepare timestamp and subject line
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String subject = "📊 Automation Execution Summary | " + environment + " | " + tag + " | " + timeStamp;

        // Fetch execution data from Hooks
        int passCount = Hooks.getPassCount();
        int failCount = Hooks.getFailCount();
        int total = Hooks.getTotalCount();
        double passPercentage = total != 0 ? (passCount * 100.0) / total : 0;
        double failPercentage = total != 0 ? (failCount * 100.0) / total : 0;
        List<String> failedScenarios = Hooks.getFailedScenarios();

        // Build the HTML message body
        StringBuilder body = new StringBuilder();
        body.append("<html><body>")
                .append("<h2>📊 <u>Automation Test Execution Summary</u></h2>")
                .append("<p><b>🌐 Environment:</b> ").append(environment).append("</p>")
                .append(" 🏷️ <b>Tag:</b> ").append(tag).append("</p>")
                .append("<p><b>⏰ Execution Time:</b> ").append(timeStamp).append("</p>")
                .append("<hr>")
                .append("<p><b>🎯 Total Scenarios Executed:</b> ").append(total).append("</p>")
                .append("<p><b>🟢 Passed Scenarios:</b> <span style='color:green;'>").append(passCount)
                .append(" (").append(String.format("%.2f", passPercentage)).append("%)</span></p>")
                .append("<p><b>🔴 Failed Scenarios:</b> <span style='color:red;'>").append(failCount)
                .append(" (").append(String.format("%.2f", failPercentage)).append("%)</span></p>")
                .append("<hr>")
                .append("<h3>⚠️ Failed Scenarios:</h3>");

        if (failedScenarios.isEmpty()) {
            body.append("<p>✅ All Scenarios Passed</p>");
        } else {
            body.append("<ul>");
            for (String scenario : failedScenarios) {
                body.append("<li>").append(scenario).append("</li>");
            }
            body.append("</ul>");
        }

        // Append closing remarks and branding
        body.append("<br><p>The detailed execution report is attached for your reference. ")
                .append("<b><i>Please Download the attached report and open it in Browser</i></b>. ")
                .append("Kindly review the report and let me know if any further clarification is required.</p>")
                .append("<h4>📌 Next Steps:</h4>")
                .append("<p><b><i>Consistently Expanding Automation Scope for Enhanced Future Efficiency</i></b></p>")
                .append("<br><p>Best Regards,<br>")
                .append("<b>Vedant Shukla</b><br>")
                .append("<b>Automation Engineer</b></p>")
                .append("</body></html>");

        // Create mail session with authentication
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            InternetAddress[] recipientAddresses = Arrays.stream(recipients)
                    .map(r -> {
                        try {
                            return new InternetAddress(r);
                        } catch (AddressException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray(InternetAddress[]::new);

            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            message.setSubject(subject);

            // Set email body and attachment
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body.toString(), "text/html; charset=utf-8");

            File latestReport = ReportParser.getLatestReport();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            if (latestReport != null) {
                attachmentPart.attachFile(latestReport);
            }

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
