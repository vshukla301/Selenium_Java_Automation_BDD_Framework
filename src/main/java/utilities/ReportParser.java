package utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

public class ReportParser {

    public static Map<String, Object> getReportSummary() {
        Map<String, Object> reportData = new HashMap<>();
        int pass = 0, fail = 0;
        List<String> failedScenarios = new ArrayList<>();

        try {
            // Dynamically find the latest Extent Report
            File latestReport = getLatestReport();

            if (latestReport == null) {
                System.out.println("No report found.");
                return reportData;
            }

            Document doc = Jsoup.parse(latestReport, "UTF-8");

            // Update selectors based on your Spark Report HTML structure
            Elements testNodes = doc.select("li.test"); // Adjust if necessary

            for (Element node : testNodes) {
                String scenarioName = node.select("span.name").text();
                String status = node.select("span.status").text();

                if (status.equalsIgnoreCase("pass")) {
                    pass++;
                } else if (status.equalsIgnoreCase("fail")) {
                    fail++;
                    failedScenarios.add(scenarioName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        reportData.put("pass", pass);
        reportData.put("fail", fail);
        reportData.put("failedScenarios", failedScenarios);

        return reportData;
    }

    // Method to get the latest Extent Report
    public static File getLatestReport() {
        File dir = new File("test-output");
        File[] files = dir.listFiles(File::isDirectory);

        if (files == null || files.length == 0) return null;

        // Sort directories based on last modified time
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        File latestDir = files[0];
        File report = new File(latestDir, "test-output/SparkReport/Spark.html");

        return report.exists() ? report : null;
    }
}
