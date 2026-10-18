package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.CurrencyUtils;
import utils.LoggerUtils;
import utils.WaitUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutOverviewPage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;

    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By finishButton = By.id("finish");
    private final By summaryTotal = By.cssSelector(".summary_total_label, [data-test='total-label'], [data-test='checkout-summary-container']");

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public boolean isLoaded() {
        return waitUtils.waitForAnyVisible(summaryTotal, finishButton).isDisplayed();
    }

    public List<String> getItemNames() {
        return driver.findElements(itemNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isProductDisplayed(String productName) {
        return getItemNames().stream().anyMatch(name -> name.equalsIgnoreCase(productName));
    }

    public String getTotalAmount() {
        String totalText = waitUtils.waitForAnyVisible(summaryTotal, finishButton).getText();
        LoggerUtils.info("Checkout total text (USD): " + totalText + " | Estimated INR: "
                + CurrencyUtils.formatEstimatedInr(totalText));
        return totalText;
    }

    public String getTotalAmountInr() {
        return CurrencyUtils.formatInr(getTotalAmount());
    }

    public void finishCheckout() {
        waitUtils.waitForAnyClickable(finishButton).click();
    }
}
