package com.salesforce.framework.pages;

import java.time.Duration;

import org.openqa.selenium.By;
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

    private static final String SIGN_IN_HOST = "login.salesforce.com";

    private static final By AUTHENTICATED_INDICATOR = By.xpath(
            "//*[contains(@class,'slds-global-header')] | //*[contains(@class,'appLauncher')] | //a[contains(@href,'/lightning/')]");

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//input[@id='username']")
    private WebElement usernameField;

    @FindBy(xpath = "//input[@id='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//input[@id='rememberUn']")
    private WebElement rememberMeCheckbox;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//div[@id='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicitWait")));
        PageFactory.initElements(driver, this);
    }

    public LoginPage enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOf(usernameField));
        field.clear();
        field.sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        WebElement field = wait.until(driver -> {
            try {
                return passwordField.isDisplayed() ? passwordField : null;
            } catch (NoSuchElementException exception) {
                return null;
            }
        });
        field.clear();
        field.sendKeys(password);
        return this;
    }

    public LoginPage clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return this;
    }

    public LoginPage submitUsername(String username) {
        return enterUsername(username).clickLogin();
    }

    public LoginPage login(String username, String password) {
        return enterUsername(username).clickLogin().enterPassword(password).clickLogin();
    }

    public LoginPage checkRememberMe() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(rememberMeCheckbox));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    public boolean isUsernameFieldDisplayed() {
        return isDisplayed(usernameField);
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }

    public boolean isRememberMeDisplayed() {
        return isDisplayed(rememberMeCheckbox);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        if (!isErrorMessageDisplayed()) {
            throw new IllegalStateException("Login error message was not displayed");
        }
        return errorMessage.getText().trim();
    }

    public boolean isAuthenticated() {
        if (getCurrentUrl().contains(SIGN_IN_HOST)) {
            return false;
        }
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(AUTHENTICATED_INDICATOR)) != null;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    private boolean isDisplayed(WebElement element) {
        try {
            return Boolean.TRUE.equals(wait.until(driver -> {
                try {
                    return element.isDisplayed();
                } catch (NoSuchElementException exception) {
                    return false;
                }
            }));
        } catch (TimeoutException exception) {
            return false;
        }
    }
}
