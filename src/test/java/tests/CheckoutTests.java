package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    @Test(priority = 1, description = "Complete checkout flow should succeed with valid fictional data")
    public void completeCheckoutFlowSuccessfully() {
        loginAsValidUser();
        inventoryPage.addProductToCart(PRODUCT_NAME);
        inventoryPage.openCart();
        cartPage.clickCheckout();
        checkoutPage.enterCheckoutInformation(FIRST_NAME, LAST_NAME, ZIP_CODE);
        checkoutPage.continueCheckout();
        checkoutOverviewPage.finishCheckout();

        Assert.assertTrue(checkoutCompletePage.isOrderConfirmed(), "The checkout completion page should appear after successful purchase.");
    }

    @Test(priority = 2, description = "Checkout overview should display the selected product")
    public void checkoutOverviewDisplaysSelectedProduct() {
        loginAsValidUser();
        inventoryPage.addProductToCart(PRODUCT_NAME);
        inventoryPage.openCart();
        cartPage.clickCheckout();
        checkoutPage.enterCheckoutInformation(FIRST_NAME, LAST_NAME, ZIP_CODE);
        checkoutPage.continueCheckout();

        Assert.assertTrue(checkoutOverviewPage.isLoaded(), "The checkout overview page should load after continuing checkout.");
        Assert.assertTrue(checkoutOverviewPage.isProductDisplayed(PRODUCT_NAME),
                "The selected product should be visible in the checkout overview.");
    }

    @Test(priority = 3, description = "Checkout should validate required fields before proceeding")
    public void checkoutValidationWithMissingFields() {
        loginAsValidUser();
        inventoryPage.addProductToCart(PRODUCT_NAME);
        inventoryPage.openCart();
        cartPage.clickCheckout();
        checkoutPage.enterCheckoutInformation("", LAST_NAME, ZIP_CODE);
        checkoutPage.continueCheckout();

        String errorText = checkoutPage.getErrorMessage();
        Assert.assertTrue(errorText.contains("First Name is required") || errorText.contains("required"),
                "The checkout form should show a validation error when a required field is missing.");
    }
}
