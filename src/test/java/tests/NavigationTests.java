package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WaitUtils;

public class NavigationTests extends BaseTest {

    @Test(priority = 1, description = "Logout should return the user to the login page")
    public void logoutFunctionalityWorks() {
        loginAsValidUser();
        WaitUtils waitUtils = new WaitUtils(driver);
        waitUtils.waitForClickable(By.id("react-burger-menu-btn")).click();
        waitUtils.waitForClickable(By.id("logout_sidebar_link")).click();

        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "The user should be logged out and returned to the login page.");
    }

    @Test(priority = 2, description = "Protected pages should require an authenticated session")
    public void protectedPagesRequireAuthentication() {
        driver.get("https://www.saucedemo.com/inventory.html");

        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "The login page should be displayed when the user tries to access a protected page without a valid session.");
    }

    @Test(priority = 3, description = "Navigation between product, cart, and checkout pages should work correctly")
    public void navigationBetweenProductsCartAndCheckoutWorks() {
        loginAsValidUser();
        inventoryPage.addProductToCart(PRODUCT_NAME);
        inventoryPage.openCart();
        Assert.assertTrue(cartPage.isLoaded(), "The cart page should load from the product listing.");

        cartPage.clickCheckout();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"), "The user should be navigated to the checkout details page.");

        checkoutPage.enterCheckoutInformation(FIRST_NAME, LAST_NAME, ZIP_CODE);
        checkoutPage.continueCheckout();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two"), "The user should reach the checkout overview page.");
    }
}
