package Hooks;

import com.*;
import com.utils.ConfigReader;
import com.utils.DriverFactory;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private WebDriver driver;

    @Before(order = 0)
    public void setup() {
        String browser = ConfigReader.getProperty("browser");
        DriverFactory.init_driver(browser);


    }

    @After(order = 0)
    public void tearDown() {
        //DriverFactory.quitDriver();
    }
}
