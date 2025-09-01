package com.utils;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KarlConnectionPage {
    WebDriver driver;
    WebDriverWait wait;

    public KarlConnectionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ✅ Reusable method
    public void selectConnection(String connectionXpathKey) {
        // open dropdown
        WebElement connectiondropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("connectiondropdown"))));
        connectiondropdown.click();

        // select connection dynamically
        WebElement option = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("karlconnection"))));
        option.click();

        // click Done
        WebElement doneBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("Donebtn"))));
        doneBtn.click();

        // validate connection added
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("connectionadded"))));

        if (message.isDisplayed()) {
            message.click();
            ScreenshotUtil.captureScreenshot("After connection done ");
        } else {
            System.out.println("Connection not done: " + connectionXpathKey);
        }
    }
}

