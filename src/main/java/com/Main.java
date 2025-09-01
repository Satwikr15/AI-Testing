package com;

import okhttp3.*;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Main {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .readTimeout(120,TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) {
        try {
            //initial query & response (seed)
            String query = "What are the segments?";
            String response = "The dataset contains three unique segments: Consumer, Corporate, and Home Office.";

            // Build prompt for Ollama
            String prompt = "You are testing a dataset-driven chatbot.\n"
                    + "Previous query: \"" + query + "\"\n"
                    + "Chatbot response: \"" + response + "\"\n"
                    + "Based on this, generate 1 new meaningful user query to test further.\n"
                    + "Return only the query in a numbered list.";

            // Create JSON body
            JSONObject json = new JSONObject();
            json.put("model", "llama3.2");  // Change if needed
            json.put("prompt", prompt);
            json.put("stream",false);

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

                    // Parse JSON and just get the "response" field
                    JSONObject obj = new JSONObject(result);
                    String newquery = obj.getString("response");

                    System.out.println("\nGenerated Test Query:\n" + newquery);
                } else {
                    System.out.println("Request failed: " + responseObj.code());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
