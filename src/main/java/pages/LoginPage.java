package pages;

import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error'], [role='alert'], .error-message-container");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public void open() {
        driver.get(ConfigReader.getBaseUrl());
        waitUtils.waitForVisible(loginButton);
    }

    public void loginAs(String username, String password) {
        WebElement usernameField = waitUtils.waitForVisible(usernameInput);
        usernameField.clear();
        usernameField.sendKeys(username);

        WebElement passwordField = waitUtils.waitForVisible(passwordInput);
        passwordField.clear();
        passwordField.sendKeys(password);

        waitUtils.waitForClickable(loginButton).click();
    }

    public String getErrorMessage() {
        return waitUtils.waitForVisible(errorMessage).getText();
    }

    public boolean isErrorDisplayed() {
        return waitUtils.isElementPresent(errorMessage) && waitUtils.waitForVisible(errorMessage).isDisplayed();
    }

    public boolean isLoginPageDisplayed() {
        return driver.getCurrentUrl().contains("saucedemo.com/")
                && waitUtils.waitForVisible(loginButton).isDisplayed();
    }
}
