package com.pages;

import com.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class KarlPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public KarlPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void openKarlUrl() {
        driver.get(ConfigReader.getProperty("karlurl"));
    }

    public boolean isKarlAppOpened() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(ConfigReader.getProperty("KarlTheStatistician"))));
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickAddConnection() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div.fixed.inset-0.flex.items-center.justify-center.bg-black.bg-opacity-50")));

            WebElement connectBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(ConfigReader.getProperty("connectyourdata"))));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", connectBtn);
            connectBtn.click();

            System.out.println("✅ 'Connect Your Data' button clicked successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to click 'Connect Your Data': " + e.getMessage());
            throw e;
        }
    }
}

