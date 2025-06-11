package testRunner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * The TestRunner class is the main entry point for executing Cucumber feature files using TestNG.
 * It extends AbstractTestNGCucumberTests to inherit Cucumber-TestNG integration behavior.
 */
@CucumberOptions(
        features = "src/test/java/feature", // Location of feature files
        glue = {"stepDefinitions"},         // Step definitions package
        monochrome = true,                  // Console output formatting (readable)
        dryRun = false,                     // Set to true to validate steps without actual execution
        tags = "@smoke",                   // Filters scenarios to run only those tagged with @sanity
        plugin = {
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", // Extent report plugin
                "rerun:target/failed_scenarios.txt"                                     // Rerun file for failed scenarios
        }
)
public class TestRunner extends AbstractTestNGCucumberTests {
    /**
     * DataProvider to supply scenarios for TestNG execution.
     * Overrides default behavior and allows for parallel execution if needed.
     *
     * @return 2D array of scenario objects
     */
    @DataProvider(parallel = false)
    @Override
    public Object[][] scenarios() {
        return super.scenarios(); // Return the default scenarios provided by AbstractTestNGCucumberTests
    }
}
