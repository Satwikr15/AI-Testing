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
            String prompt = "You are testing a dataset-driven chatbot.\n"
                    + "Previous query: \"" + prevQuery + "\"\n"
                    + "Chatbot response: \"" + prevResponse + "\"\n"
                    + "Based on this, generate 1 new meaningful user query to test further.\n"
                    + "Return only the query, no explanation.";

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
