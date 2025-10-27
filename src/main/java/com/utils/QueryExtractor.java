package com.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class QueryExtractor {
    private WebDriver driver;
    private WebDriverWait wait;

    private WebElement Epromptspace;
    private WebElement sendbtn;

    public QueryExtractor(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void extractionQuery(String ExtractionQuery) {
        //extraction Query
        Epromptspace = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("promptspace"))));
        Epromptspace.sendKeys(ExtractionQuery);

        sendbtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("Sendbtn"))));
        sendbtn.click();

        WebElement startvisualization = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("startvisualizationbtn"))));
        if (startvisualization.isDisplayed()) {
            startvisualization.click();
        } else {
            System.out.println("startvisualization is not visualized !");
        }
    }

//    public String sendQueries(String query) {
//        try {
//            // 1. Enter query
//            WebElement promptspace = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath(ConfigReader.getProperty("promptspace"))));
//            promptspace.clear();
//            promptspace.sendKeys(query);
//
//            WebElement sendbtn = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath(ConfigReader.getProperty("Sendbtn"))));
//            sendbtn.click();
//
//            // 2. Count current responses
//            int initialCount = driver.findElements(By.xpath(
//                            "//h1[contains(text(),'Summary')]/following-sibling::p[@class='mb-2 font-medium'] | //p[@class='mb-2 font-medium']"))
//                    .size();
//
//            // 3. Wait until new response appears (up to 3 minutes)
//            wait.withTimeout(Duration.ofSeconds(180)).until(ExpectedConditions
//                    .numberOfElementsToBeMoreThan(By.xpath(
//                                    "//h1[contains(text(),'Summary')]/following-sibling::p[@class='mb-2 font-medium'] | //p[@class='mb-2 font-medium']"),
//                            initialCount));
//
//            String chatbotResponse = "";
//            long endTime = System.currentTimeMillis() + 180000; // 3 minutes max
//
//            while (System.currentTimeMillis() < endTime) {
//                try {
//                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
//
//                    List<WebElement> summaryList = driver.findElements(By.xpath(
//                            "//h1[contains(text(),'Summary')]/following-sibling::p[@class='mb-2 font-medium']"));
//                    WebElement lastResponse = null;
//
//                    if (!summaryList.isEmpty()) {
//                        lastResponse = summaryList.get(summaryList.size() - 1);
//                    } else {
//                        List<WebElement> responseList = driver.findElements(
//                                By.xpath("//p[@class='mb-2 font-medium']"));
//                        if (!responseList.isEmpty())
//                            lastResponse = responseList.get(responseList.size() - 1);
//                    }
//
//                    if (lastResponse != null) {
//                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", lastResponse);
//
//                        // Wait until text stops changing for 3s
//                        String lastText = "";
//                        String currentText = "";
//                        long stableStart = System.currentTimeMillis();
//
//                        while (System.currentTimeMillis() - stableStart < 3000 && System.currentTimeMillis() < endTime) {
//                            currentText = lastResponse.getText().trim();
//                            if (!currentText.equals(lastText)) {
//                                stableStart = System.currentTimeMillis();
//                                lastText = currentText;
//                            }
//                            Thread.sleep(500);
//                        }
//
//                        chatbotResponse = currentText.trim();
//                    }
//
//                    if (!chatbotResponse.isEmpty())
//                        break;
//
//                    Thread.sleep(1000);
//
//                } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            if (chatbotResponse.isEmpty())
//                throw new RuntimeException("Extracted response is still empty after waiting 180s!");
//
//            System.out.println("Latest chatbot response (UI Extracted): " + chatbotResponse);
//            return chatbotResponse;
//
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Error while extracting response", e);
//        }
//    }

//    public String sendQueries(String query) {
//        try {
//            // 1.Enter query
//            WebElement promptspace = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ConfigReader.getProperty("promptspace"))));
//            promptspace.clear();
//            promptspace.sendKeys(query);
//
//            WebElement sendbtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ConfigReader.getProperty("Sendbtn"))));
//            sendbtn.click();
//
//// 1. Count existing text responses
//            List<WebElement> initialTextResponses = driver.findElements(
//                    By.xpath("//p[@class='mb-2 font-medium' and not(.//img)]"));
//            int initialCount = initialTextResponses.size();
//
//// 2. Wait for a new text response
//            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(180));
//            longWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
//                    By.xpath("//p[@class='mb-2 font-medium' and not(.//img)]"), initialCount));
//
//// 3. Get all text-only responses
//            List<WebElement> allTextResponses = driver.findElements(
//                    By.xpath("//p[@class='mb-2 font-medium' and not(.//img)]"));
//
//// 4. Get the latest text response
//            WebElement latestResponse = allTextResponses.get(allTextResponses.size() - 1);
//            String latestText = latestResponse.getText();
//
//            System.out.println("Latest text response: " + latestText);
//
//
//            // 5.Wait until text stabilizes
//            String previousText = "";
//            String currentText = "";
//            long stableStart = System.currentTimeMillis();
//
//            while (System.currentTimeMillis() - stableStart < 3000) {
//                currentText = latestResponse.getText().trim();
//                if (!currentText.equals(previousText) && !currentText.isEmpty()) {
//                    stableStart = System.currentTimeMillis();
//                    previousText = currentText;
//                }
//                Thread.sleep(500);
//            }
//
//            if (currentText.isEmpty()) {
//                throw new RuntimeException("Response is empty after waiting!");
//            }
//
//            // 6.Check for "Oops! Something went wrong" or similar error
//            if (currentText.toLowerCase().contains("oops") || currentText.toLowerCase().contains("something went wrong")) {
//                throw new RuntimeException("❌ Chatbot returned an error message: " + currentText);
//            }
//
//            System.out.println("✅ Extracted response: " + currentText);
//            return currentText;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error while extracting chatbot response", e);
//        }
//    }

public String sendQueries(String query) {
    try {
        System.out.println("🟢 Sending query: " + query);

        // 1️⃣ Enter query text
        WebElement promptspace = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(ConfigReader.getProperty("promptspace"))));
        promptspace.clear();
        promptspace.sendKeys(query);

        WebElement sendbtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(ConfigReader.getProperty("Sendbtn"))));
        sendbtn.click();

        // 2️⃣ Count initial text responses
        List<WebElement> initialResponses = driver.findElements(
                By.xpath("//p[@class='mb-2 font-medium' and not(.//img)]"));
        int initialCount = initialResponses.size();
        System.out.println("Initial response count: " + initialCount);

        // 3️⃣ Wait until new response appears
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(180));
        longWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath("//p[@class='mb-2 font-medium' and not(.//img)]"), initialCount));

        // 4️⃣ Collect all current responses
        List<WebElement> responses = driver.findElements(
                By.xpath("//p[@class='mb-2 font-medium' and not(.//img)]"));
        if (responses.isEmpty()) {
            throw new RuntimeException("❌ No text responses found even after waiting.");
        }

        WebElement latestResponse = responses.get(responses.size() - 1);
        String latestText = latestResponse.getText().trim();
        System.out.println("🟢 Raw extracted text: " + latestText);

        // 5️⃣ Wait until text stops changing (stabilizes)
        String prevText = "";
        long stableStart = System.currentTimeMillis();

        while (System.currentTimeMillis() - stableStart < 3000) {
            String currentText = latestResponse.getText().trim();
            if (!currentText.equals(prevText) && !currentText.isEmpty()) {
                stableStart = System.currentTimeMillis();
                prevText = currentText;
            }
            Thread.sleep(500);
        }

        if (latestText.isEmpty()) {
            throw new RuntimeException("❌ Response is empty after stabilization period.");
        }

        // 6️⃣ Detect chatbot error messages
        if (latestText.toLowerCase().contains("oops") ||
                latestText.toLowerCase().contains("something went wrong")) {
            throw new RuntimeException("❌ Chatbot returned error: " + latestText);
        }

        System.out.println("✅ Final Extracted Response: " + latestText);
        return latestText;

    } catch (Exception e) {
        // 🔍 Print exact cause before wrapping
        e.printStackTrace();
        throw new RuntimeException("Error while extracting chatbot response → " + e.getMessage(), e);
    }
}


    public void waitUntilAppears(WebDriver driver, WebDriverWait wait) {
        try {
            // XPath for the span with text 'Karl'
            By karlElement = By.xpath("//span[contains(@class,'text-2xl') and contains(text(),'Karl')]");

            // Wait until the element is visible on the page
            wait.until(ExpectedConditions.visibilityOfElementLocated(karlElement));

            System.out.println("✅ 'Karl' element appeared successfully.");
        } catch (TimeoutException e) {
            throw new RuntimeException("❌ 'Karl' element did not appear within the wait time.", e);
        }
    }

}
