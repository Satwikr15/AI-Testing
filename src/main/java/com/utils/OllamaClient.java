package com.utils;

import okhttp3.*;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class OllamaClient {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .readTimeout(120,TimeUnit.SECONDS)
            .build();

    public static String generateNextQuery(String prevQuery, String prevResponse) {

        System.out.println("----------------------------------------------");
        System.out.println("Input Query to OLLAMA : "+prevQuery);
        System.out.println("Input Respsonse to OLLAMA : "+prevResponse);
        System.out.println("----------------------------------------------");
        try {
            String prompt = "You are testing a dataset-driven chatbot that answers analytical questions based on sales data."
                    + "The dataset includes columns such as Row ID, Order ID, Order Date, Ship Date, Ship Mode, Customer ID, "
                    + "Customer Name, Segment, Country, City, State, Postal Code, Region, Product ID, Category, Sub-Category, "
                    + "Product Name, Sales, Quantity, Discount, and Profit."
                    + "The chatbot should be able to handle descriptive analysis, comparative analysis, and location-based insights."
                    + "Previous query: \"" + prevQuery + "\""
                    + "Chatbot response: \"" + prevResponse + "\""
                    + "Based on this, generate one new meaningful user query that tests the chatbot’s ability to analyze or visualize the dataset further. "
                    + "The query should sound natural, like a real user asking about sales, profit, customers, categories, or regions."
                    + "Return only the query text, with no explanation or extra formatting.";


            JSONObject json = new JSONObject();
            json.put("model", "llama3.2");
            json.put("prompt", prompt);
            json.put("stream", false);

            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(OLLAMA_URL)
                    .post(body)
                    .build();

            try (Response responseObj = client.newCall(request).execute()) {
                if (responseObj.isSuccessful()) {
                    String result = responseObj.body().string();
                    JSONObject obj = new JSONObject(result);
                    return obj.getString("response").trim();
                } else {
                    System.out.println("Request failed: " + responseObj.code());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
