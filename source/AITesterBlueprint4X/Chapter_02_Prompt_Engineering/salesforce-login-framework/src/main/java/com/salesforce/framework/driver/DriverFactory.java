package com.salesforce.framework.driver;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.salesforce.framework.config.ConfigReader;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        try {
            WebDriver existingDriver = DRIVER.get();
            if (existingDriver != null) {
                return existingDriver;
            }
            String browser = ConfigReader.get("browser");
            if (!"chrome".equalsIgnoreCase(browser)) {
                throw new IllegalArgumentException("Unsupported browser configured: " + browser);
            }
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-allow-origins=*");
            if (ConfigReader.getBoolean("headless")) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicitWait")));
            DRIVER.set(driver);
            return driver;
        } catch (WebDriverException | IllegalArgumentException exception) {
            throw new RuntimeException("Unable to initialize the WebDriver session", exception);
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver session is not initialized for the current thread");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } catch (WebDriverException exception) {
            throw new RuntimeException("Unable to terminate the WebDriver session", exception);
        } finally {
            DRIVER.remove();
        }
    }
}
