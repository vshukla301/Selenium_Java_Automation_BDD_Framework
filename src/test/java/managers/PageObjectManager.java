package managers;

import org.openqa.selenium.WebDriver;
import pageObjects.LoginPage;

/**
 * PageObjectManager is responsible for instantiating and managing page objects.
 * It holds a reference to the WebDriver and ensures that each page object is created
 * only once during the test execution lifecycle.
 *
 * This promotes better memory management and avoids redundant object creation in
 * the Page Object Model (POM) based framework.
 */

public class PageObjectManager {
   public WebDriver driver;
    LoginPage loginPage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getloginPageObject() {
        loginPage = new LoginPage(driver);
        return loginPage;
    }

}
