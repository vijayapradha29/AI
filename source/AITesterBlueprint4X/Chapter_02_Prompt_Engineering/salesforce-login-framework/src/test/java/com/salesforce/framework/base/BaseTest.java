package com.salesforce.framework.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.salesforce.framework.config.ConfigReader;
import com.salesforce.framework.driver.DriverFactory;

public class BaseTest {

    private WebDriver driver;

    @BeforeTest(alwaysRun = true)
    public void setUpSuite() {
        try {
            ConfigReader.get("baseUrl");
        } catch (RuntimeException exception) {
            throw new RuntimeException("Test suite configuration could not be loaded", exception);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void initDriver() {
        try {
            driver = DriverFactory.createDriver();
            driver.get(ConfigReader.get("baseUrl"));
        } catch (RuntimeException exception) {
            throw new RuntimeException("Test setup failed while launching the browser", exception);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            DriverFactory.quitDriver();
        } finally {
            driver = null;
        }
    }

    public WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not available outside of a test method");
        }
        return driver;
    }
}
