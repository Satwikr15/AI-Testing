package Hooks;

import com.utils.ConfigReader;
import com.utils.DriverFactory;
import com.utils.ScreenshotUtil;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private WebDriver driver;

    @Before(order = 0)
    public void setup() {
        String browser = ConfigReader.getProperty("browser");
        driver = DriverFactory.init_driver(browser);
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        driver = DriverFactory.getDriver();

        // 🔹 If scenario failed, capture screenshot and mark failure
        if (scenario.isFailed()) {
            try {
                // Using built-in screenshot
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");

                // Optional - using your ScreenshotUtil if you have it
                //ScreenshotUtil.captureScreenshot(driver, scenario.getName());

                System.out.println("❌ Scenario failed: " + scenario.getName());
            } catch (Exception e) {
                System.out.println("⚠️ Could not capture screenshot: " + e.getMessage());
            }
        }

        //Always close driver at the end
        //DriverFactory.quitDriver();
    }
}
