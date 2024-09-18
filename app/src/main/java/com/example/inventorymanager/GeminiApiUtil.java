package com.example.inventorymanager;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiApiUtil {
    private static final String TAG = "GeminiApiUtil";
    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Replace with your actual API key
    private static final String MODEL_NAME = "gemini-pro-vision";
    private static final GenerativeModel model = new GenerativeModel(MODEL_NAME, API_KEY);
    private static final GenerativeModelFutures modelFutures = GenerativeModelFutures.from(model);
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public interface GeminiApiCallback {
        void onSuccess(String result);
        void onFailure(String errorMessage);
    }

    public static void analyzeImage(Bitmap image, String prompt, GeminiApiCallback callback) {
        if (image == null) {
            Log.e(TAG, "Image is null");
            callback.onFailure("Image is null");
            return;
        }

        Log.d(TAG, "Image size: " + image.getWidth() + "x" + image.getHeight());

        try {
            Content content = new Content.Builder()
                    .addText(prompt)
                    .addImage(image)
                    .build();

            CompletableFuture.supplyAsync(() -> {
                try {
                    Log.d(TAG, "Sending request to Gemini API");
                    GenerateContentResponse response = modelFutures.generateContent(content).get();
                    Log.d(TAG, "Received response from Gemini API");
                    return response;
                } catch (Exception e) {
                    Log.e(TAG, "Error in Gemini API call", e);
                    throw new RuntimeException("Error in Gemini API call: " + e.getMessage(), e);
                }
            }, executor).thenAccept(result -> {
                String generatedText = result.getText();
                Log.d(TAG, "Generated text: " + generatedText);
                callback.onSuccess(generatedText);
            }).exceptionally(t -> {
                Log.e(TAG, "Error in Gemini API process", t);
                callback.onFailure("Error: " + t.getMessage());
                return null;
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating content for Gemini API", e);
            callback.onFailure("Error creating content: " + e.getMessage());
        }
    }
}