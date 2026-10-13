package com.salesforce.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.salesforce.framework.base.BaseTest;
import com.salesforce.framework.pages.LoginPage;

public class InvalidLoginTest extends BaseTest {

    private static final String STANDARD_ERROR_FRAGMENT = "Please check your username and password";

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
                { "invalid.user@example.com", "WrongPassword123", true },
                { "wronguser@example.com", "WrongPassword123", true },
                { "", "WrongPassword123", false },
                { "wronguser@example.com", "", false },
                { "", "", false }
        };
    }

    @Test(dataProvider = "invalidCredentials")
    public void verifyInvalidLogin(String username, String password, boolean standardErrorExpected) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for invalid login");
        String actualError = loginPage.getErrorMessage();
        Assert.assertFalse(actualError.isBlank(), "Error message is empty");
        if (standardErrorExpected) {
            Assert.assertTrue(actualError.contains(STANDARD_ERROR_FRAGMENT),
                    "Unexpected error message: " + actualError);
        }
        Assert.assertTrue(loginPage.getCurrentUrl().contains("login.salesforce.com"),
                "User was incorrectly redirected away from the login page");
    }
}
