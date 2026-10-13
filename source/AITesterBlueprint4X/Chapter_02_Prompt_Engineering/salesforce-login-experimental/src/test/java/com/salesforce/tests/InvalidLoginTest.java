package com.salesforce.tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.salesforce.framework.base.BaseTest;
import com.salesforce.framework.config.ConfigReader;
import com.salesforce.framework.pages.LoginPage;

public class InvalidLoginTest extends BaseTest {

    private static final String USERNAME_VALIDATION_FRAGMENT = "Please enter your username";

    @Test
    public void verifyEmptyUsernameShowsValidation() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.submitUsername("");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Validation message was not displayed");
        String message = loginPage.getErrorMessage();
        Assert.assertTrue(message.contains(USERNAME_VALIDATION_FRAGMENT),
                "Unexpected validation message: " + message);
        Assert.assertFalse(loginPage.isAuthenticated(), "An empty username must not authenticate");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
                { "invalid.user@example.invalid", "WrongPassword123" },
                { "wronguser@example.invalid", "WrongPassword123" }
        };
    }

    @Test(dataProvider = "invalidCredentials")
    public void verifyInvalidCredentials(String username, String password) {
        if (!ConfigReader.getBoolean("allowCredentialTests")) {
            throw new SkipException(
                    "Credential-submitting tests are not authorized; pass -DallowCredentialTests=true");
        }
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(username);
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "An error message was not displayed for invalid credentials");
        Assert.assertFalse(loginPage.getErrorMessage().isBlank(), "Error message is empty");
        Assert.assertFalse(loginPage.isAuthenticated(), "Invalid credentials must not authenticate");
    }
}
