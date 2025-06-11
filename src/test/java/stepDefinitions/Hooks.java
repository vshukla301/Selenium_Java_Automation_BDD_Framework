package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import managers.FileReaderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.ScenarioContext;
import utilities.EmailConnector;
import utilities.ScenarioResult;
import utils.EmailUtil;
import utils.TestContextSetup;

import javax.mail.Store;
import java.util.ArrayList;
import java.util.List;

/**
 * Hooks class defines Cucumber lifecycle hooks that are executed before and after scenarios or feature execution.
 * It also manages the collection and processing of scenario results and performs post-execution tasks like sending emails.
 *
 * Key Functionalities:
 *
 * 1. Scenario Tracking:
 *    - Stores the current executing `Scenario` object for use in other components.
 *    - Extracts and sets the current feature name into `ScenarioContext`.
 *
 * 2. Scenario Results Management:
 *    - After each scenario, captures its name and execution status into a list (`scenarioResults`).
 *    - Provides static methods to retrieve execution metrics:
 *      - `getPassCount()` - Count of passed scenarios.
 *      - `getFailCount()` - Count of failed scenarios.
 *      - `getTotalCount()` - Total executed scenarios.
 *      - `getFailedScenarios()` - List of names of failed scenarios.
 *
 * 3. Post Execution Handling:
 *    - `@AfterAll` hook uses `Runtime.getRuntime().addShutdownHook()` to delay execution of email reporting.
 *    - Connects to Gmail using credentials from config and sends an email with the execution report.
 *
 * Notes:
 * - `@Before` and `@After` hooks are Cucumber lifecycle annotations.
 * - The static context (`scenarioResults`, `scenario`) makes this class unsuitable for parallel test execution.
 * - Email connection is established using a separate utility (`EmailConnector`) and sent via `EmailUtil`.
 * - Consider refactoring for thread-safety in parallel execution environments.
 */

public class Hooks {

    private static Logger logger = LoggerFactory.getLogger(Hooks.class);
    private static Scenario scenario;
    private static List<ScenarioResult> scenarioResults = new ArrayList<>();
    public TestContextSetup testContextSetup;

    public Hooks(TestContextSetup testContextSetup){
        this.testContextSetup=testContextSetup;
    }

    @Before
    public void before(Scenario scenario) {
        Hooks.scenario = scenario;
    }

    public static Scenario getScenario() {
        return scenario;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        String featureName = scenario.getUri().toString().split("/")[scenario.getUri().toString().split("/").length - 1];
        ScenarioContext.setCurrentFeature(featureName);
    }

    @After
    public void after_Scenario(Scenario scenario) {
        ScenarioResult result = new ScenarioResult(scenario.getName(), scenario.getStatus().toString());
        scenarioResults.add(result);
    }

    public static List<ScenarioResult> getScenarioResults() {
        return scenarioResults;
    }

    public static int getPassCount() {
        return (int) scenarioResults.stream().filter(r -> r.getStatus().equalsIgnoreCase("PASSED")).count();
    }

    public static int getFailCount() {
        return (int) scenarioResults.stream().filter(r -> r.getStatus().equalsIgnoreCase("FAILED")).count();
    }

    public static int getTotalCount() {
        return scenarioResults.size();
    }

    public static List<String> getFailedScenarios() {
        List<String> failedScenarios = new ArrayList<>();
        scenarioResults.stream()
                .filter(r -> r.getStatus().equalsIgnoreCase("FAILED"))
                .forEach(r -> failedScenarios.add(r.getScenarioName()));
        return failedScenarios;
    }

    @AfterAll
    public static void afterExecution() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Thread.sleep(5000);
                String username = FileReaderManager.getInstance().getConfigReader().getEmailUserName();
                String password = FileReaderManager.getInstance().getConfigReader().getEmailPassword();

                // Connect to Gmail
                Store store = EmailConnector.connectToGmail(username, password, 0);

                // Send Report Email
                EmailUtil.sendEmailWithReport();
                System.out.println("Post-execution process completed!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @After
    public void tearDown(){
        testContextSetup.baseTest.initializeDriver().quit();
    }

}
