package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.time.Duration;

public class CheckoutPage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[role='alert'], .error-message-container, [data-test='error']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public void enterCheckoutInformation(String firstName, String lastName, String postalCode) {
        waitUtils.waitForUrlContains("checkout-step-one");
        waitUtils.waitForVisible(firstNameInput).clear();
        driver.findElement(firstNameInput).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(postalCodeInput).sendKeys(postalCode);
    }

    public void continueCheckout() {
        waitUtils.waitForClickable(continueButton).click();
    }

    public String getErrorMessage() {
        return waitUtils.waitForAnyVisible(errorMessage).getText();
    }
}
