package com.utils;


import com.utils.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;

public class ScreenshotUtil {

    public static void captureScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(src, new File("./screenshots/" + name + "1.png"));
            System.out.println("Screenshot captured: " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
