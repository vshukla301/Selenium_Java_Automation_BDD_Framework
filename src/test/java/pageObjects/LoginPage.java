package pageObjects;

import managers.FileReaderManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import reUsableComponent.SeleniumHelper;

public class LoginPage extends SeleniumHelper {
	WebDriver driver;

	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	private final By TEXT_USERNAME = By.xpath("//input[@name='username']");
	private final By TEXT_PASSWORD = By.xpath("//input[@name='password']");
	private final By BTN_LOGIN = By.xpath("//input[@value='Log In']");
	private final By VALIDATION_MESSAGE_LOGIN_ERROR = By.xpath("//h1/following-sibling::p");
	private final By VALIDATION_ACCOUNT_SERVICE_LABEL = By.xpath("//h2");



	public void loginWithValidCredentials(){
		waitForElementToBeVisible(TEXT_USERNAME);
		enterText(TEXT_USERNAME, FileReaderManager.getInstance().getConfigReader().getValidUserId());
		waitForElementToBeVisible(TEXT_PASSWORD);
		enterText(TEXT_PASSWORD, FileReaderManager.getInstance().getConfigReader().getValidPassword());
	}

	public void validationMessage(String expected){
		verifyContainsText(VALIDATION_MESSAGE_LOGIN_ERROR, expected);
	}

	public void validationAccountServiceLABEL(String expected){
		verifyContainsText(VALIDATION_ACCOUNT_SERVICE_LABEL, expected);
	}

	public void loginWithInvalidUser(){
		waitForElementToBeVisible(TEXT_USERNAME);
		enterText(TEXT_USERNAME, FileReaderManager.getInstance().getConfigReader().getInvalidUserId());
		waitForElementToBeVisible(TEXT_PASSWORD);
		enterText(TEXT_PASSWORD, FileReaderManager.getInstance().getConfigReader().getInvalidPassword());
	}

	public void clickLogin(){
		waitForElementToBeVisible(BTN_LOGIN);
		click(BTN_LOGIN);
	}


}
