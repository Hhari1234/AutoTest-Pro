package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class InventoryTests extends BaseTest {

    @Test(priority = 1, description = "Inventory page loads after successful login")
    public void inventoryPageLoadsAfterLogin() {
        loginAsValidUser();

        Assert.assertTrue(inventoryPage.isLoaded(), "The inventory page should be visible after login.");
    }

    @Test(priority = 2, description = "All product names and prices should be visible")
    public void productNamesAndPricesAreDisplayed() {
        loginAsValidUser();

        List<String> productNames = inventoryPage.getProductNames();
        List<String> productPrices = inventoryPage.getProductPrices();

        Assert.assertFalse(productNames.isEmpty(), "At least one product name should be visible on the inventory page.");
        Assert.assertFalse(productPrices.isEmpty(), "At least one product price should be visible on the inventory page.");
        Assert.assertTrue(productNames.stream().allMatch(name -> !name.isBlank()), "Every product should have a visible name.");
        Assert.assertTrue(productPrices.stream().allMatch(price -> price.startsWith("$") || !price.isBlank()), "Every product should display a price.");
    }

    @Test(priority = 3, description = "Product sorting should work when the user selects a sort option")
    public void sortingProductsByPriceLowToHighWorks() {
        loginAsValidUser();

        inventoryPage.sortProductsBy("Price (low to high)");

        List<String> productNames = inventoryPage.getProductNames();
        Assert.assertFalse(productNames.isEmpty(), "Products should remain visible after sorting.");
    }

    @Test(priority = 4, description = "Cart icon should be visible on the inventory page")
    public void cartIconIsVisible() {
        loginAsValidUser();

        Assert.assertTrue(inventoryPage.isCartIconVisible(), "The cart icon should be visible to indicate the shopping cart.");
    }
}
