package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(priority = 1, description = "Login with valid demo credentials")
    public void loginWithValidCredentials() {
        loginPage.loginAs(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should load after a successful login.");
    }

    @Test(priority = 2, description = "Login with invalid password should show an error")
    public void loginWithInvalidPasswordShouldShowError() {
        loginPage.loginAs(VALID_USERNAME, INVALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error message should be displayed for invalid login credentials.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("do not match any user in this service"),
                "The login error message should explain that the username and password do not match.");
    }

    @Test(priority = 3, description = "Empty username should show a validation error")
    public void loginWithEmptyUsernameShouldShowError() {
        loginPage.loginAs("", VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "A validation error should be displayed when the username is empty.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "The error message should clearly state that the username is required.");
    }

    @Test(priority = 4, description = "Empty password should show a validation error")
    public void loginWithEmptyPasswordShouldShowError() {
        loginPage.loginAs(VALID_USERNAME, "");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "A validation error should be displayed when the password is empty.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
                "The error message should clearly state that the password is required.");
    }

    @Test(priority = 5, description = "Verify appropriate error message is generated for invalid login")
    public void verifyAppropriateErrorMessageIsDisplayed() {
        loginPage.loginAs("locked_out_user", VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "The locked-out user should trigger an error message.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Sorry, this user has been locked out"),
                "The error message should clearly explain the locked-out account scenario.");
    }
}
