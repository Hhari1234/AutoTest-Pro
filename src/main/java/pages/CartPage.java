package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CartPage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;

    private final By cartItems = By.cssSelector(".cart_item");
    private final By cartItemName = By.cssSelector(".inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public boolean isLoaded() {
        return waitUtils.waitForVisible(checkoutButton).isDisplayed();
    }

    public List<String> getCartItemNames() {
        return driver.findElements(cartItemName).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isProductDisplayed(String productName) {
        return getCartItemNames().stream().anyMatch(name -> name.equalsIgnoreCase(productName));
    }

    public void removeProduct(String productName) {
        String productKey = normalizeProductName(productName);
        By removeButton = By.cssSelector("button[data-test='remove-" + productKey + "']");
        waitUtils.waitForClickable(removeButton).click();
    }

    public void clickCheckout() {
        waitUtils.waitForClickable(checkoutButton).click();
        waitUtils.waitForUrlContains("checkout-step-one");
    }

    public boolean isCartEmpty() {
        return driver.findElements(cartItems).isEmpty();
    }

    private String normalizeProductName(String productName) {
        return productName.toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
