package base;

import driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.CartPage;
import pages.CheckoutCompletePage;
import pages.CheckoutOverviewPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.LoggerUtils;
import utils.ScreenshotUtils;

import java.lang.reflect.Method;

public class BaseTest {
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected CartPage cartPage;
    protected CheckoutPage checkoutPage;
    protected CheckoutOverviewPage checkoutOverviewPage;
    protected CheckoutCompletePage checkoutCompletePage;

    protected static final String VALID_USERNAME = "standard_user";
    protected static final String VALID_PASSWORD = "secret_sauce";
    protected static final String INVALID_PASSWORD = "wrongPassword";
    protected static final String PRODUCT_NAME = "Sauce Labs Backpack";
    protected static final String FIRST_NAME = "QA";
    protected static final String LAST_NAME = "Automation";
    protected static final String ZIP_CODE = "12345";

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        driver = DriverFactory.createDriver();
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        checkoutOverviewPage = new CheckoutOverviewPage(driver);
        checkoutCompletePage = new CheckoutCompletePage(driver);

        loginPage.open();
        LoggerUtils.info("Starting test: " + method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());
            LoggerUtils.warn("Failure screenshot captured: " + screenshotPath);
        }

        DriverFactory.quitDriver();
        LoggerUtils.info("Completed test: " + result.getName() + " with status " + result.getStatus());
    }

    protected void loginAsValidUser() {
        loginPage.loginAs(VALID_USERNAME, VALID_PASSWORD);
    }
}
