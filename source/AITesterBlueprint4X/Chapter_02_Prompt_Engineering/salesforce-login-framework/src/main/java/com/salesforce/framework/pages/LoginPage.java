package com.salesforce.framework.pages;

import java.time.Duration;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.salesforce.framework.config.ConfigReader;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//input[@id='username']")
    private WebElement usernameField;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@id='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//input[@id='rememberUn']")
    private WebElement rememberMeCheckbox;

    @FindBy(xpath = "//div[@id='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicitWait")));
        PageFactory.initElements(driver, this);
    }

    public LoginPage typeUsername(String username) {
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameField)).clear();
            usernameField.sendKeys(username);
            return this;
        } catch (TimeoutException | NoSuchElementException exception) {
            throw new RuntimeException("Username field was not available for input", exception);
        }
    }

    public LoginPage typePassword(String password) {
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordField)).clear();
            passwordField.sendKeys(password);
            return this;
        } catch (TimeoutException | NoSuchElementException exception) {
            throw new RuntimeException("Password field was not available for input", exception);
        }
    }

    public LoginPage clickLogin() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
            return this;
        } catch (TimeoutException | ElementClickInterceptedException | NoSuchElementException exception) {
            throw new RuntimeException("Login button was not clickable", exception);
        }
    }

    public LoginPage login(String username, String password) {
        return typeUsername(username).typePassword(password).clickLogin();
    }

    public LoginPage checkRememberMe() {
        try {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(rememberMeCheckbox));
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
            return this;
        } catch (TimeoutException | ElementClickInterceptedException | NoSuchElementException exception) {
            throw new RuntimeException("Remember Me checkbox was not clickable", exception);
        }
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText().trim();
        } catch (TimeoutException | NoSuchElementException exception) {
            throw new RuntimeException("Login error message was not displayed", exception);
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    public boolean isUsernameFieldDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(usernameField)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    public boolean isPasswordFieldDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(passwordField)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    public boolean isLoginButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loginButton)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    public boolean isRememberMeDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(rememberMeCheckbox)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
