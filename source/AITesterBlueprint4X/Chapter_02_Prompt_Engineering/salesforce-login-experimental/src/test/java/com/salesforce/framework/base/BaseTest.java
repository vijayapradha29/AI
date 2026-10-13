package com.salesforce.framework.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.salesforce.framework.config.ConfigReader;
import com.salesforce.framework.driver.DriverFactory;

public class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    private WebDriver driver;

    @BeforeTest(alwaysRun = true)
    public void setUpSuite() {
        ConfigReader.getRequired("browser");
        ConfigReader.getRequired("baseUrl");
        ConfigReader.getInt("explicitWait");
    }

    @BeforeMethod(alwaysRun = true)
    public void openLoginPage() {
        driver = DriverFactory.createDriver();
        driver.get(ConfigReader.getRequired("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getName());
        }
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

    private void captureScreenshot(String testName) {
        try {
            File source = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            Path target = Paths.get("target", "screenshots", testName + ".png");
            Files.createDirectories(target.getParent());
            Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | RuntimeException exception) {
            LOGGER.warn("Unable to capture a screenshot for {}", testName, exception);
        }
    }
}
