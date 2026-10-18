package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.CurrencyUtils;
import utils.LoggerUtils;
import utils.WaitUtils;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class InventoryPage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;

    private final By header = By.cssSelector(".title");
    private final By productNames = By.cssSelector(".inventory_item_name");
    private final By productPrices = By.cssSelector(".inventory_item_price");
    private final By sortDropdown = By.cssSelector("select[data-test='product-sort-container']");
    private final By cartIcon = By.cssSelector("#shopping_cart_container");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public boolean isLoaded() {
        return waitUtils.waitForVisible(header).isDisplayed();
    }

    public List<String> getProductNames() {
        return waitUtils.waitForVisibleAll(productNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<String> getProductPrices() {
        List<String> rawPrices = waitUtils.waitForVisibleAll(productPrices)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        LoggerUtils.info("Website product prices (USD): " + rawPrices + " | Estimated INR values: "
                + rawPrices.stream()
                .map(CurrencyUtils::formatEstimatedInr)
                .collect(Collectors.joining(", ")));
        return rawPrices;
    }

    public List<String> getProductPricesInr() {
        return getProductPrices().stream()
                .map(CurrencyUtils::formatInr)
                .collect(Collectors.toList());
    }

    public void sortProductsBy(String label) {
        Select dropdown = new Select(waitUtils.waitForVisible(sortDropdown));
        dropdown.selectByVisibleText(label);
    }

    public boolean isCartIconVisible() {
        return waitUtils.waitForVisible(cartIcon).isDisplayed();
    }

    public String getCartBadgeText() {
        if (driver.findElements(cartBadge).isEmpty()) {
            return "0";
        }
        return waitUtils.waitForVisible(cartBadge).getText();
    }

    public void addProductToCart(String productName) {
        String productKey = normalizeProductName(productName);
        By addButton = By.cssSelector("button[data-test='add-to-cart-" + productKey + "']");
        waitUtils.waitForClickable(addButton).click();
    }

    public void openCart() {
        waitUtils.waitForClickable(cartIcon).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d ->
                d.getCurrentUrl().contains("cart.html") || d.findElements(By.id("checkout")).size() > 0);
    }

    private String normalizeProductName(String productName) {
        return productName.toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
