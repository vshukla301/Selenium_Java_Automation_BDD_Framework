package reUsableComponent;

import managers.FileReaderManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

/**
 * SeleniumHelper provides a collection of utility methods to abstract and simplify
 * common WebDriver actions such as clicking, sending keys, dropdown selection,
 * window handling, and element synchronization.
 *
 * Features:
 * - Multiple click strategies (WebDriver, Actions, JavaScriptExecutor)
 * - Text verification with exact and partial match support
 * - Dropdown interaction via Select class
 * - Wait management using WebDriverWait and ExpectedConditions
 * - Window handling for parent/child switching
 * - Custom logging and structured exception handling for better debugging
 *
 * Enhances:
 * - Code reusability
 * - Readability of test code
 * - Reliability of UI interactions across different test environments
 *
 * Dependencies:
 * - WebDriver instance passed via constructor
 * - Externalized wait time via FileReaderManager for configurability
 */

public class SeleniumHelper {

    private WebDriver driver;
    private WebDriverWait wait;
    String parent;
    private static final long EXPLICIT_WAIT = FileReaderManager.getInstance().getConfigReader().getExplicitWait();
    private static final Logger logger = LoggerFactory.getLogger(SeleniumHelper.class);

    public SeleniumHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT));
    }

    // ================================
    // WAIT UTILITIES
    // ================================
    public void waitForElementToBeClickable(By loc) {
        wait.until(ExpectedConditions.elementToBeClickable(loc));
    }

    public void waitForElementToBeVisible(By loc) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
    }

    public void waitForElementToBeVisible(WebElement ele) {
        wait.until(ExpectedConditions.visibilityOf(ele));
    }

    // ================================
    // CLICK METHODS
    // ================================
    public void clickElement(By loc) {
        try {
            waitForElementToBeClickable(loc);
            driver.findElement(loc).click();
        } catch (Exception e) {
            throw new RuntimeException("Unable to click element: " + loc, e);
        }
    }

    public void clickElement(WebElement ele) {
        try {
            waitForElementToBeClickable((By) ele);
            ele.click();
        } catch (Exception e) {
            throw new RuntimeException("Unable to click element", e);
        }
    }

    public void click(By loc) {
        try {
            WebElement ele = driver.findElement(loc);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ele);
        } catch (Exception e) {
            throw new RuntimeException("JavaScript click failed: " + loc, e);
        }
    }

    public void click(By loc, int timeToWait) {
        try {
            Thread.sleep(timeToWait * 1000);
            WebElement ele = driver.findElement(loc);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ele);
        } catch (Exception e) {
            throw new RuntimeException("JavaScript click failed: " + loc, e);
        }
    }

    public void click(WebElement loc) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loc);
        } catch (Exception e) {
            throw new RuntimeException("JavaScript click failed: " + loc, e);
        }
    }

    public void verifyTextUsingContains(By loc, String expectedText) {
        try {
            String actualText = driver.findElement(loc).getText();
            Assert.assertTrue(actualText.contains(expectedText));
        } catch (Exception e) {
            throw new RuntimeException("Unable to get Text Value");
        }
    }

    public void clickUsingActions(By loc) {
        try {
            WebElement ele = driver.findElement(loc);
            new Actions(driver).moveToElement(ele).click().perform();
        } catch (Exception e) {
            throw new RuntimeException("Action click failed: " + loc, e);
        }
    }

    public void pageOneDown() {
        try {
            new Actions(driver).sendKeys(Keys.PAGE_DOWN).perform();
        } catch (Exception e) {
            throw new RuntimeException("Scrolling one time down failed");
        }
    }


    public void clickUsingActions(WebElement loc) {
        try {

            new Actions(driver).moveToElement(loc).click().perform();
        } catch (Exception e) {
            throw new RuntimeException("Action click failed: " + loc, e);
        }
    }

    public void clearUsingActions(WebElement loc) {
        try {

            new Actions(driver).sendKeys(Keys.chord(Keys.CONTROL + "a")).sendKeys(Keys.BACK_SPACE).click().perform();
        } catch (Exception e) {
            throw new RuntimeException("Action click failed: " + loc, e);
        }
    }

    public void clearUsingActions(By loc) {
        try {
            WebElement ele = driver.findElement(loc);

            new Actions(driver).moveToElement(ele).click().sendKeys(Keys.chord(Keys.CONTROL + "a")).sendKeys(Keys.BACK_SPACE).click().perform();
        } catch (Exception e) {
            throw new RuntimeException("Action click failed: " + loc, e);
        }
    }

    public void clickUsingJavaScriptExecutor(WebElement loc) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", loc);
        } catch (Exception e) {
            throw new RuntimeException("Unable to click");
        }
    }

    public void chatbotElementClick(WebElement ele) {
        try {
            Thread.sleep(2000);
            waitForElementToBeVisible(ele);
            ele.click();
        } catch (Exception e) {
            throw new RuntimeException("Unable to click");
        }
    }

    // ================================
    // INPUT METHODS
    // ================================
    public void enterText(By loc, String text) {
        try {
            WebElement ele = driver.findElement(loc);
            ele.click();
//            clickUsingActions(loc);
            ele.clear();

            ele.sendKeys(text);
        } catch (Exception e) {
            throw new RuntimeException("Unable to enter text in: " + loc, e);
        }
    }

    public void enterTextWithoutClick(By loc, String text) {
        try {
            WebElement ele = driver.findElement(loc);
            ele.sendKeys(text);
        } catch (Exception e) {
            throw new RuntimeException("Unable to enter text in: " + loc, e);
        }
    }

    public void enterText(WebElement loc, String text) {
        try {
            loc.clear();
            loc.sendKeys(text);
        } catch (Exception e) {
            throw new RuntimeException("Unable to enter text in: " + loc, e);
        }
    }

    public void pressEnter(By loc) {
        try {
            driver.findElement(loc).sendKeys(Keys.ENTER);
        } catch (Exception e) {
            throw new RuntimeException("Unable to press Enter on: " + loc, e);
        }
    }

    public void pressEnter(WebElement loc) {
        try {
            loc.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            throw new RuntimeException("Unable to press Enter on: " + loc, e);
        }
    }

    public int validateWebElementCount(By loc) {
        List<WebElement> count = driver.findElements(loc);
        return count.size();
    }

    public void pressEscButton() {
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.ESCAPE);
    }

    public void windowScrollToBottom() {
        try {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
        } catch (Exception e) {
            throw new RuntimeException("Unable to scroll to the Bottom");
        }
    }

    public void verifyPageSourceContainsText(String expectedText) {
        try {
            Assert.assertTrue(driver.getPageSource().contains(expectedText));
        } catch (Exception e) {
            throw new RuntimeException("Unable to get Text Value");
        }
    }

    public void selectDataFromList(By loc, String expected) {
        List<WebElement> list = driver.findElements(loc);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().equalsIgnoreCase(expected)) {
                list.get(i).click();
                break;
            }
        }
    }

    // ================================
    // VERIFICATION METHODS
    // ================================
    public void verifyText(By loc, String expectedText) {
        try {
            String actualText = driver.findElement(loc).getText().trim();
            Assert.assertEquals(actualText, expectedText.trim(), "Text mismatch!");
        } catch (Exception e) {
            throw new RuntimeException("Text verification failed: " + loc, e);
        }
    }

    public void verifyText(WebElement ele, String[] expectedTexts) {
        try {
            String actualText = ele.getText().trim();
            System.out.println(actualText);
            if (actualText == null || actualText.isEmpty()) {
                throw new RuntimeException("Element text is null or empty");
            }

            boolean matchFound = false;
            for (String expectedText : expectedTexts) {
                if (actualText.contains(expectedText.trim())) {
                    matchFound = true;
                    break;
                }
            }
            Assert.assertTrue(matchFound, "The actual text does not match any of the expected texts.");
        } catch (Exception e) {
            throw new RuntimeException("Unable to verify if the element's text contains any of the expected texts", e);
        }
    }

    public void verifyText(By ele, String[] expectedTexts) {
        try {
            String actualText = driver.findElement(ele).getText().trim();
            if (actualText == null || actualText.isEmpty()) {
                throw new RuntimeException("Element text is null or empty");
            }

            boolean matchFound = false;
            for (String expectedText : expectedTexts) {
                if (actualText.contains(expectedText.trim())) {
                    matchFound = true;
//                    break;
                }
            }
            Assert.assertTrue(matchFound, "The actual text does not match any of the expected texts.");
        } catch (Exception e) {
            throw new RuntimeException("Unable to verify if the element's text contains any of the expected texts", e);
        }
    }

    public void verifyTextBreak(By ele, String[] expectedTexts) {
        try {
            String actualText = driver.findElement(ele).getText().trim();
            if (actualText == null || actualText.isEmpty()) {
                throw new RuntimeException("Element text is null or empty");
            }

            boolean matchFound = false;
            for (String expectedText : expectedTexts) {
                if (actualText.contains(expectedText.trim())) {
                    matchFound = true;
                    break;
                }
            }
            Assert.assertTrue(matchFound, "The actual text does not match any of the expected texts.");
        } catch (Exception e) {
            throw new RuntimeException("Unable to verify if the element's text contains any of the expected texts", e);
        }
    }

    public void verifyText(By ele, String[] expectedTexts, int timeToWait) {
        try {
            Thread.sleep(timeToWait * 1000);
            String actualText = driver.findElement(ele).getText().trim();
            if (actualText == null || actualText.isEmpty()) {
                throw new RuntimeException("Element text is null or empty");
            }

            boolean matchFound = false;
            for (String expectedText : expectedTexts) {
                if (actualText.contains(expectedText.trim())) {
                    matchFound = true;
//                    break;
                }
            }
            Assert.assertTrue(matchFound, "The actual text does not match any of the expected texts.");
        } catch (Exception e) {
            throw new RuntimeException("Unable to verify if the element's text contains any of the expected texts", e);
        }
    }

    public void verifyContainsText(By loc, String expectedText) {
        try {
            String actualText = driver.findElement(loc).getText().trim();
            Assert.assertTrue(actualText.contains(expectedText.trim()), "Text does not contain expected value!");
        } catch (Exception e) {
            throw new RuntimeException("Text verification failed: " + loc, e);
        }
    }

    public void verifyText(WebElement ele, String expectedText) {
        try {
            String actualText = ele.getText();
            Assert.assertEquals(actualText.trim(), expectedText.trim());
        } catch (Exception e) {
            throw new RuntimeException("Unable to get Text Value");
        }
    }

    // ================================
    // DROPDOWN METHODS
    // ================================
    public void selectByVisibleText(By loc, String visibleText) {
        try {
            new Select(driver.findElement(loc)).selectByVisibleText(visibleText);
        } catch (Exception e) {
            throw new RuntimeException("Dropdown selection failed: " + loc, e);
        }
    }

    public void selectByIndex(By loc, int index) {
        try {
            new Select(driver.findElement(loc)).selectByIndex(index);
        } catch (Exception e) {
            throw new RuntimeException("Dropdown selection failed: " + loc, e);
        }
    }

    public void selectByValue(By loc, String value) {
        try {
            new Select(driver.findElement(loc)).selectByValue(value);
        } catch (Exception e) {
            throw new RuntimeException("Dropdown selection failed: " + loc, e);
        }
    }

    // ================================
    // WINDOW OPERATIONS
    // ================================
    public void switchToChildWindow() {
        parent = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(parent)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    public void switchToParentWindow() {
        driver.switchTo().window(parent);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void pressEnterByLoc(By loc) {
        try {
            driver.findElement(loc).sendKeys(Keys.ENTER);
        } catch (Exception e) {
            throw new RuntimeException("Unable to press Enter key on the element with locator: ");
        }
    }

}

