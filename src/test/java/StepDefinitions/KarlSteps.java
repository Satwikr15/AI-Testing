package StepDefinitions;

import com.utils.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KarlSteps {
    WebDriver driver= DriverFactory.getDriver();
    WebDriverWait wait =new WebDriverWait(driver, Duration.ofSeconds(30));

    @Given("Navigate to url")
    public void navigate_to_url() {
        String url= ConfigReader.getProperty("karlurl");
        driver.get(url);
        try{
            WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("KarlTheStatistician"))));
            if(element.isDisplayed()){
                System.out.println("bro, ur in karl application");
            }
            else{
                System.out.println("no bro, ur not in karl application");
            }
        }
        catch (NoSuchElementException e){
            System.out.println("❌ Validation failed: Element not found!");
        }
    }

    @When("Click on Add Connection")
    public void click_on_add_connection() {
        WebElement addconnectionbtn=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("addconnection"))));
        addconnectionbtn.click();
        WebElement beforeconn= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("beforeconn"))));
        if(beforeconn.isDisplayed()){
            ScreenshotUtil.captureScreenshot("Before connection done");
        }

    }

    @When("user conversation with Karl")
    public void user_conversation_with_karl() {
        KarlConnectionPage karlConnectionPage=new KarlConnectionPage(driver);
        QueryExtractor queryExtractor=new QueryExtractor(driver,wait);
        karlConnectionPage.selectConnection("karlconnection");

        queryExtractor.extractionQuery("get all superstore data");
        String query ="how many segments are there ?";
        String response=queryExtractor.sendQueries(query);

        for(int i=0;i<5;i++){
            String newQuery = OllamaClient.generateNextQuery(query,response);
            System.out.println("New Query from Ollama : " + newQuery);
            System.out.println("<-------------------------------------->");

            String newResponse = queryExtractor.sendQueries(newQuery);

            query=newQuery;
            response=newResponse;
        }
    }
}