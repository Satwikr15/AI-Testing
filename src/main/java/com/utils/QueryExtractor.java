package com.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class QueryExtractor {
    private WebDriver driver;
    private WebDriverWait wait;

    private WebElement Epromptspace;
    private WebElement sendbtn;

    public QueryExtractor(WebDriver driver, WebDriverWait wait){
        this.driver=driver;
        this.wait=wait;
    }

    public void extractionQuery(String ExtractionQuery){
        //extraction Query
        Epromptspace=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("promptspace"))));
        Epromptspace.sendKeys(ExtractionQuery);

        sendbtn=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("Sendbtn"))));
        sendbtn.click();

        WebElement startvisualization=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConfigReader.getProperty("startvisualizationbtn"))));
        if(startvisualization.isDisplayed()){
            startvisualization.click();
        }
        else {
            System.out.println("startvisualization is not visualized !");
        }
    }

    public String sendQueries(String query) {
        try {
            // 1. Enter query
            WebElement promptspace = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath(ConfigReader.getProperty("promptspace"))
                    )
            );
            promptspace.clear();
            promptspace.sendKeys(query);

            WebElement sendbtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath(ConfigReader.getProperty("Sendbtn"))
                    )
            );
            sendbtn.click();

            // 2. Get count before sending
            int initialCount = driver.findElements(
                    By.xpath("//h1[contains(text(),'Summary')]/following-sibling::p[@class='mb-2 font-medium'] | //p[@class='mb-2 font-medium']")
            ).size();

            // 3. Wait for new response (Summary or Normal)
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.xpath("//h1[contains(text(),'Summary')]/following-sibling::p[@class='mb-2 font-medium'] | //p[@class='mb-2 font-medium']"),
                    initialCount
            ));

            String chatbotResponse = "";
            // 4. Retry loop to handle empty/stale responses
            for (int i = 0; i < 5; i++) {
                try {
                    // Prefer "Summary" first
                    List<WebElement> summaryList = driver.findElements(
                            By.xpath("//h1[contains(text(),'Summary')]/following-sibling::p[@class='mb-2 font-medium']")
                    );

                    if (!summaryList.isEmpty()) {
                        chatbotResponse = summaryList.get(0).getText().trim(); // always first Summary
                    } else {
                        // Fallback → last normal response
                        List<WebElement> responseList = driver.findElements(
                                By.xpath("//p[@class='mb-2 font-medium']")
                        );
                        if (!responseList.isEmpty()) {
                            chatbotResponse = responseList.get(responseList.size() - 1).getText().trim();
                        }
                    }

                    if (!chatbotResponse.isEmpty()) break; // got a valid response
                    Thread.sleep(500);

                } catch (org.openqa.selenium.StaleElementReferenceException se) {
                    // re-fetch and retry in next iteration
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (chatbotResponse.isEmpty()) {
                throw new RuntimeException("Extracted response is still empty after retries!");
            }

            System.out.println("Latest chatbot response (UI Extracted): " + chatbotResponse);
            return chatbotResponse;

        } catch (RuntimeException e) {
            throw new RuntimeException("Error while extracting response", e);
        }
    }



}
