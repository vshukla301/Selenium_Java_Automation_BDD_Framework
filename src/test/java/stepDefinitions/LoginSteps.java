package stepDefinitions;

import enums.LoginPageConstants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageObjects.LoginPage;
import utils.TestContextSetup;

public class LoginSteps {
    public LoginPage loginPage;
    public TestContextSetup testContextSetup;
    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);

    public LoginSteps(TestContextSetup testContextSetup) {
        this.testContextSetup = testContextSetup;
        this.loginPage = testContextSetup.pageObjectManager.getloginPageObject();
    }

    @Given("verify user is on Login Page")
    public void verify_user_is_on_login_page() {
        logger.info("Browser Launched and URL Opened");
    }

    @When("user enters Valid username and password")
    public void user_enters_valid_username_and_password() {
        logger.info("User Entered Credentials");
        loginPage.loginWithValidCredentials();
        logger.info("User Entered Credentials");
    }

    @When("click on Login Button")
    public void click_on_login_button() {
        logger.info("User Clicking on Login Button");
        loginPage.clickLogin();

    }

    @Then("user should see the Home page")
    public void user_should_see_the_home_page() {
        logger.info("User is on Home Page");
        loginPage.validationAccountServiceLABEL(LoginPageConstants.ACCOUNT_SERVICES_LABEL);
        logger.info("Home page validation completed");
    }

    @When("user enters Invalid username and password")
    public void user_enters_invalid_username_and_password() {
        logger.info("User Entered In Valid Credentials");
        loginPage.loginWithInvalidUser();

    }

    @Then("user should see the error Message")
    public void user_should_see_the_error_message() {
        logger.info("User Got Error");
        loginPage.validationMessage(LoginPageConstants.VALIDATION_MESSAGE);
    }

}
