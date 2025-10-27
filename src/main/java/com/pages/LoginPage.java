package com.pages;

import com.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void openAppUrl() {
        driver.get(ConfigReader.getProperty("appUrl"));
    }

    public void loginToMicrosoft() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(ConfigReader.getProperty("button.microsoftlogin")))).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath(ConfigReader.getProperty("username.microsoft"))))
                    .sendKeys(ConfigReader.getProperty("username"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(ConfigReader.getProperty("button.next")))).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath(ConfigReader.getProperty("password.microsoft"))))
                    .sendKeys(ConfigReader.getProperty("password"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(ConfigReader.getProperty("button.signin")))).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(ConfigReader.getProperty("button.yes")))).click();

            System.out.println("Microsoft login successful.");
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            throw e;
        }
    }
}

