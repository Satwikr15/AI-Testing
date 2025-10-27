package com.utils;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;

public class KarlConnectionPage {
    WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));


    public KarlConnectionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ✅ Reusable method
    public void selectConnection(String connectionXpathKey,String DataBase) {
        // Step 1: open dropdown
        WebElement connectiondropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty(connectionXpathKey))));
        connectiondropdown.click();

        // Step 2: Choose the option
        String optionToSelect = DataBase; // can be Karl, Karl-Fabric, etc.
        WebElement connectionOption = wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//ul[@class='p-dropdown-items']//li[.//span[normalize-space(text())='" + optionToSelect + "']]")));
        connectionOption.click();

        WebElement ConnectBtn=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("Connectbtn"))));
        ConnectBtn.click();

//        // validate connection added
//        WebElement message = wait.until(
//                ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("ValidationText"))));

//        if (message.isDisplayed()) {
//            message.click();
//            ScreenshotUtil.captureScreenshot("After connection done ");
//        } else {
//            System.out.println("Connection not done: " + connectionXpathKey);
//        }
    }
}

