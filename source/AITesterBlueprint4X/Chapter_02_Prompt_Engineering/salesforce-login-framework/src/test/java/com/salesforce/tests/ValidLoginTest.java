package com.salesforce.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.salesforce.framework.base.BaseTest;
import com.salesforce.framework.config.ConfigReader;
import com.salesforce.framework.pages.LoginPage;

public class ValidLoginTest extends BaseTest {

    @DataProvider(name = "validCredentials")
    public Object[][] validCredentials() {
        return new Object[][] {
                { ConfigReader.get("validUsername"), ConfigReader.get("validPassword") }
        };
    }

    @Test(priority = 1)
    public void verifyLoginPageUiElements() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field is not displayed");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field is not displayed");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button is not displayed");
        Assert.assertTrue(loginPage.isRememberMeDisplayed(), "Remember Me checkbox is not displayed");
    }

    @Test(priority = 2, dataProvider = "validCredentials")
    public void verifyValidLogin(String username, String password) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.checkRememberMe().login(username, password);
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(), "Error message displayed for valid credentials");
        Assert.assertFalse(loginPage.getCurrentUrl().contains("login.salesforce.com"),
                "User was not redirected away from the login page");
    }
}
