package utilities;

/**
 * ScenarioResult is a simple data container class (POJO) used to store
 * the outcome of a test scenario.
 *
 * Fields:
 * - scenarioName: Holds the name or title of the executed test scenario.
 * - status: Represents the result status of the scenario (e.g., "Passed", "Failed").
 *
 * This class is typically used in test reporting mechanisms to log or analyze
 * individual scenario results after execution.
 */

public class ScenarioResult {
    private String scenarioName;
    private String status;

    public ScenarioResult(String scenarioName, String status) {
        this.scenarioName = scenarioName;
        this.status = status;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public String getStatus() {
        return status;
    }
}
