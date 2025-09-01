package StepDefinitions;

import com.utils.CSVUtil;
import com.utils.ConfigReader;
import com.utils.DriverFactory;
import com.utils.ScreenshotUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import io.cucumber.java.en.*;

import java.time.Duration;
import java.util.List;


public class DokGPTSteps {

    WebDriver driver = DriverFactory.getDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    @Given("login process")
    public void login_process() throws InterruptedException {
        String url = ConfigReader.getProperty("appUrl");
        driver.get(url);
        Thread.sleep(2000);
//        login process
        WebElement MicrosoftLogin=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("button.microsoftlogin"))));
        MicrosoftLogin.click();

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("username.microsoft"))));
        usernameField.sendKeys(ConfigReader.getProperty("username"));

        WebElement NextButton= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("button.next"))));
        NextButton.click();

        WebElement passwordField=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("password.microsoft"))));
        passwordField.sendKeys(ConfigReader.getProperty("password"));

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ConfigReader.getProperty("button.signin")))));
        loginButton.click();

        WebElement YesButton = wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ConfigReader.getProperty("button.yes")))));
        YesButton.click();

        Thread.sleep(2000);
    }

    @When("navigate to Ai workbench")
    public void navigate_to_ai_workbench() {
        String url = ConfigReader.getProperty("appUrl");
        driver.get(url);
        WebElement aiWorkbenchIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ConfigReader.getProperty("AIworkbench.icon"))));
        aiWorkbenchIcon.click();
    }

    @When("Search for DokGPT application and click on it")
    public void search_and_click_dokgpt() {
        WebElement dokGptCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(ConfigReader.getProperty("DokGPTCard"))));
        dokGptCard.click();
    }

    @And("user sends general questions from file {string} and validates responses")
    public void send_general_questions(String csvPath) {
        sendQueries(csvPath, "question");
    }

    @And("user sends video-type questions from file {string} and validates responses")
    public void send_video_type_questions(String csvPath) {
        sendQueries(csvPath,  "Video");
        disableModeButton("Video");
    }

    @And("user sends web search questions from file {string} and validates responses")
    public void send_web_search_questions(String csvPath) {
        sendQueries(csvPath,  "Web Search");
        disableModeButton("Search");
    }

    // ====================== Core Method ======================
    public void sendQueries(String csvPath,  String mode) {
        try {
            // Switch to iframe
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath(ConfigReader.getProperty("iframe"))));
            driver.switchTo().frame(iframe);

            // Open config
            WebElement settingsIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(ConfigReader.getProperty("Configuration_icon"))));
            settingsIcon.click();

            // Set config from properties
            selectDropdownOption(ConfigReader.getProperty("dropdown.Content_Modularity"), ConfigReader.getProperty("contentModularity"));
            selectDropdownOption(ConfigReader.getProperty("dropdown.llmModel"), ConfigReader.getProperty("llm"));
            selectDropdownOption(ConfigReader.getProperty("dropdown.Embedding_Models"), ConfigReader.getProperty("embeddingModel"));
            selectDropdownOption(ConfigReader.getProperty("dropdown.reranking_model"), ConfigReader.getProperty("rerankingModel"));
            selectDropdownOption(ConfigReader.getProperty("dropdown.vector_db"), ConfigReader.getProperty("vectorDB"));
            selectDropdownOption(ConfigReader.getProperty("dropdown.namespaces"), ConfigReader.getProperty("namespace"));

            TextareaField("Instructions", ConfigReader.getProperty("instructionText"));
            driver.findElement(By.tagName("body")).click();  // Close config

            List<String> questions = CSVUtil.readQuestions(csvPath);  // filePath is your CSV file path
            int index = 1;

            for (String query : questions) {
                if (query == null || query.trim().isEmpty()) continue ;

                // Enable the correct button
                if (!mode.equalsIgnoreCase("question")) {
                    enableModeButton(mode);  // "video", "web", etc.
                }


                // Enter query
                WebElement inputBox = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(ConfigReader.getProperty("Input_Textarea"))));
                inputBox.sendKeys(query);
                inputBox.sendKeys(Keys.ENTER);

                // Wait for response
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".response-loader")));
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".response-loader")));
                } catch (TimeoutException te) {
                    Thread.sleep(2000);
                }

                // get latest response
                WebElement responseContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("(//div[contains(@class, 'html-response')])[last()]")));
                String innerHTML = responseContainer.getAttribute("innerHTML").toLowerCase();
                boolean hasHtmlTags = innerHTML.contains("<h1>") || innerHTML.contains("<p>") || innerHTML.contains("<ul>");
                boolean hasErrorText = innerHTML.contains("something went wrong");

                String borderColor = hasErrorText || !hasHtmlTags ? "red" : "green";
                String highlightColor = "yellow";
                String screenshotName = (hasErrorText || !hasHtmlTags)
                        ? "Error_" + mode + "_" + index
                        : "Response_" + mode + "_" + index;

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].style.border='2px solid " + borderColor + "';" +
                                "arguments[0].style.background='" + highlightColor + "';" +
                                "arguments[0].style.transition='all 0.5s ease-in-out';",
                        responseContainer
                );

                ScreenshotUtil.captureScreenshot(screenshotName);

                if (hasErrorText || !hasHtmlTags) {
                    System.err.println("Invalid " + mode + " response [" + index + "]: " + query);
                } else {
                    System.out.println("Valid " + mode + " response [" + index + "]: " + query);
                }

                index++; //for console print
            }

        } catch (Exception e) {
            ScreenshotUtil.captureScreenshot("Unhandled_Exception_" + mode);
            throw new RuntimeException("Exception while processing " + mode + " queries", e);
        } finally {
            try {
                driver.switchTo().defaultContent();
            } catch (Exception ignored) {}
        }
    }

    // ====================== Button Logic ======================

    public void navigateDoKGPT(){
        WebElement dokGptCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(ConfigReader.getProperty("DokGPTCard"))));
        dokGptCard.click();
    }

    private String currentMode = "";

    public void enableModeButton(String mode) {
        if (mode.equalsIgnoreCase(currentMode)) {
            System.out.println("ℹ️ Mode already enabled: " + mode);
            return;
        }

        switch (mode) {
            case "Video": {
                By videoButtonLocator = By.xpath("//button[@title='Video']");
                WebElement videoBtn = wait.until(ExpectedConditions.presenceOfElementLocated(videoButtonLocator));

                // Click only if not already active

                wait.until(ExpectedConditions.elementToBeClickable(videoBtn)).click();
                wait.until(ExpectedConditions.attributeContains(videoBtn, "class", "bg-blue-500"));
                System.out.println("✅ Video mode enabled");

                break;
            }

            case "Web Search": {
                By webBtnLocator = By.cssSelector("button[title='Search the web']");
                WebElement webBtn = wait.until(ExpectedConditions.presenceOfElementLocated(webBtnLocator));

                // Click only if not already active

                wait.until(ExpectedConditions.elementToBeClickable(webBtn)).click();
                wait.until(ExpectedConditions.attributeContains(webBtn, "class", "bg-blue-500"));
                System.out.println("✅ Web Search mode enabled");

                break;
            }

            default:
                throw new IllegalArgumentException("❌ Unsupported mode: " + mode);
        }

        // Update current mode
        currentMode = mode;
    }

    public void disableModeButton(String mode) {
        try {
            // Switch to the iframe first
            WebElement iframe = driver.findElement(By.cssSelector("iframe[src*='document-copilot']"));
            driver.switchTo().frame(iframe);

            // Wait for the button (Video or Web Search) to be clickable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement modeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space(text())='" + mode + "'] and contains(@class, 'hover:bg-blue-500')]")
            ));

            // Click the mode button to disable
            modeButton.click();

            System.out.println("❌ " + mode + " mode disabled");
        } catch (Exception e) {
            System.err.println("⚠️ Failed to disable mode: " + mode + " - " + e.getMessage());
        } finally {
            // Always switch back to the main content
            driver.switchTo().defaultContent();
        }
    }

    public void selectDropdownOption(String selectId, String optionText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.name(selectId)));
        new Select(dropdown).selectByVisibleText(optionText);
    }

    public void TextareaField(String fieldId, String fieldText) {
        WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='absolute right-2 top-2 bg-white p-1 z-20']")));
        editIcon.click();

        WebElement instructionsModalBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'fixed') and contains(@class,'inset-0')]//textarea[@name='" + fieldId + "']")));
        instructionsModalBox.clear();
        instructionsModalBox.sendKeys(fieldText);

        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'fixed') and contains(@class,'inset-0')]//button[text()='Confirm']")));
        confirmButton.click();
    }
}
