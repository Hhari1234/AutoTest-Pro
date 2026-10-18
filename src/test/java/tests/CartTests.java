package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTests extends BaseTest {

    @Test(priority = 1, description = "Adding a product updates the cart badge")
    public void addProductToCartUpdatesBadge() {
        loginAsValidUser();
        inventoryPage.addProductToCart(PRODUCT_NAME);

        Assert.assertEquals(inventoryPage.getCartBadgeText(), "1",
                "The cart badge should update to show one selected product.");
    }

    @Test(priority = 2, description = "Selected product should appear in cart")
    public void selectedProductAppearsInCart() {
        loginAsValidUser();
        inventoryPage.addProductToCart(PRODUCT_NAME);
        inventoryPage.openCart();

        Assert.assertTrue(cartPage.isLoaded(), "The cart page should load after opening the cart.");
        Assert.assertTrue(cartPage.isProductDisplayed(PRODUCT_NAME), "The selected product should appear in the cart.");
    }

    @Test(priority = 3, description = "Removing a product should remove it from the cart")
    public void removeProductFromCart() {
        loginAsValidUser();
        inventoryPage.addProductToCart(PRODUCT_NAME);
        inventoryPage.openCart();
        cartPage.removeProduct(PRODUCT_NAME);

        Assert.assertTrue(cartPage.isCartEmpty(), "The cart should be empty after removing the only selected product.");
    }

    @Test(priority = 4, description = "All products removed should leave the cart empty")
    public void cartBecomesEmptyWhenAllProductsAreRemoved() {
        loginAsValidUser();
        inventoryPage.addProductToCart(PRODUCT_NAME);
        inventoryPage.addProductToCart("Sauce Labs Bike Light");
        inventoryPage.openCart();
        cartPage.removeProduct(PRODUCT_NAME);
        cartPage.removeProduct("Sauce Labs Bike Light");

        Assert.assertTrue(cartPage.isCartEmpty(), "The cart should be empty after all added products are removed.");
    }
}
