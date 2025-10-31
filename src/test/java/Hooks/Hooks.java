package Hooks;

import com.utils.ConfigReader;
import com.utils.DriverFactory;
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

        // Capture screenshot if scenario failed
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
                System.out.println("❌ Scenario failed: " + scenario.getName());
            } catch (Exception e) {
                System.out.println("⚠️ Could not capture screenshot: " + e.getMessage());
            }
        }

        // Always quit driver after scenario
        DriverFactory.quitDriver();
    }
}
