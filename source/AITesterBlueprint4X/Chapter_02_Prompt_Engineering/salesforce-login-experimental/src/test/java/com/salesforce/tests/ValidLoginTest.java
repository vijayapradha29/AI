package com.salesforce.tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.salesforce.framework.base.BaseTest;
import com.salesforce.framework.config.ConfigReader;
import com.salesforce.framework.pages.LoginPage;

public class ValidLoginTest extends BaseTest {

    @Test
    public void verifyLoginPageUiElements() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field is not displayed");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button is not displayed");
        Assert.assertTrue(loginPage.isRememberMeDisplayed(), "Remember Me checkbox is not displayed");
    }

    @Test
    public void verifyValidLogin() {
        String username = ConfigReader.get("validUsername");
        String password = ConfigReader.get("validPassword");
        if (username.isBlank() || password.isBlank()) {
            throw new SkipException("Valid credentials were not supplied; pass -DvalidUsername and -DvalidPassword");
        }
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.checkRememberMe();
        loginPage.login(username, password);
        Assert.assertTrue(loginPage.isAuthenticated(),
                "A post-login indicator was not found; authentication could not be confirmed");
    }
}
