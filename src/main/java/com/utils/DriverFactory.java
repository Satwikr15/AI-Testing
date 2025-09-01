package com.utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverFactory {

    private static WebDriver driver;

    //Initialize driver only if not already initialized
    public static WebDriver init_driver(String browser) {
        if (driver == null) {
            System.out.println("Initializing browser: " + browser);

            switch (browser.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            getDriver().manage().deleteAllCookies();
            getDriver().manage().window().maximize();

        } else {
            System.out.println("Browser session already active. Reusing it.");
        }

        return driver;
    }
//	 public static WebDriver init_driver(String browser) {
//
//		 System.out.println("browser value is: " + browser);
//
//		 if (browser.equals("chrome")) {
//			 WebDriverManager.chromedriver().setup();
//			 ChromeOptions options=new ChromeOptions();
//			options.addArguments("--remote-allow-origins=*");
////			options.addArguments("--verbose");
////			options.setBrowserVersion(ChromeDriverLogLevel.ALL);
//			 options.setExperimentalOption("debuggerAddress", "localhost:9988");
//			 driver = new ChromeDriver(options);
//		 }else {
//			 System.out.println("Please pass the correct browser value: " + browser);
//		 }
//
//		 driver.manage().deleteAllCookies();
//		 driver.manage().window().maximize();
//		 return driver;
//
//	 }



    public static WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call init_driver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("Browser session closed.");
        }
    }
}

