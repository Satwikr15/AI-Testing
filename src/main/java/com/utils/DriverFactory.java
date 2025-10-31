package com.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverFactory {

    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static WebDriver init_driver(String browser) {
        System.out.println("Initializing browser: " + browser);

        if (tlDriver.get() == null) {
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    // ✅ Headless mode for CI
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--window-size=1920,1080");
                    tlDriver.set(new ChromeDriver(options));
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    tlDriver.set(new FirefoxDriver());
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    tlDriver.set(new EdgeDriver());
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            getDriver().manage().deleteAllCookies();
            getDriver().manage().window().maximize();
        } else {
            System.out.println("Browser session already active. Reusing it.");
        }

        return getDriver();
    }

    public static WebDriver getDriver() {
        if (tlDriver.get() == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call init_driver() first.");
        }
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
            System.out.println("Browser session closed.");
        }
    }
}
