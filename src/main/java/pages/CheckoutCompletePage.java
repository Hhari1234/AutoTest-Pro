package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class CheckoutCompletePage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;

    private final By completionHeader = By.cssSelector(".complete-header");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public boolean isOrderConfirmed() {
        return waitUtils.waitForVisible(completionHeader).getText().contains("Thank you for your order!");
    }

    public void clickBackHome() {
        waitUtils.waitForClickable(backHomeButton).click();
    }
}
