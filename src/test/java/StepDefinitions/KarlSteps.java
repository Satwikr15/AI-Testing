package StepDefinitions;

import com.pages.KarlPage;
import com.pages.LoginPage;
import com.utils.*;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class KarlSteps {
    WebDriver driver = DriverFactory.getDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    LoginPage loginPage = new LoginPage(driver);
    KarlPage karlPage = new KarlPage(driver);

    @Given("login process of flip")
    public void loginProcessOfFlip() throws InterruptedException {
        loginPage.openAppUrl();
        loginPage.loginToMicrosoft();
    }
    @When("Navigate to url")
    public void navigate_to_url() {
        karlPage.openKarlUrl();
        if (karlPage.isKarlAppOpened()) {
            System.out.println("Karl application loaded successfully.");
        } else {
            System.out.println("Karl app validation failed.");
        }
    }

    @When("Click on Add Connection")
    public void click_on_add_connection() {
        karlPage.clickAddConnection();
    }

    @When("agent conversation with Karl")
    public void agent_conversation_with_karl() {
        KarlConnectionPage karlConnectionPage = new KarlConnectionPage(driver);
        QueryExtractor queryExtractor = new QueryExtractor(driver, wait);
        karlConnectionPage.selectConnection("connectiondropdown", "Karl");

        //learn here if (QueryExtractor) used , adding static in function
        queryExtractor.waitUntilAppears(driver, wait);

        //Extraction query
        queryExtractor.extractionQuery("get all superstore data");

        //Initial query for conversation # Mandatory field
        String query = "Plot the total sales from each category  /  Plot the total sales from each subcategory";
        String response = queryExtractor.sendQueries(query);

        for (int i = 0; i < 15; i++) {
            String newQuery = OllamaClient.generateNextQuery(query, response);
            System.out.println("New Query from Ollama : " + newQuery);
            System.out.println("<-------------------------------------->");

            String newResponse = queryExtractor.sendQueries(newQuery);

            query = newQuery;
            response = newResponse;
        }
    }

    @And("user conversation with Karl")
    public void userConversationWithKarl() {
        KarlConnectionPage karlConnectionPage = new KarlConnectionPage(driver);
        QueryExtractor queryExtractor = new QueryExtractor(driver, wait);

        // Select the connection
        karlConnectionPage.selectConnection("connectiondropdown", "Karl");

        // Start visualization / initial query
        queryExtractor.extractionQuery("get all superstore data");

        // Load questions from CSV using classpath (server-ready)
        List<String> questions = CSVUtil.readQuestions("testdata/queries.csv");

        // Loop through each query
        for (String query : questions) {
            System.out.println("Sending query: " + query);
            String response = queryExtractor.sendQueries(query);
            System.out.println("Response: " + response);
        }
    }


}