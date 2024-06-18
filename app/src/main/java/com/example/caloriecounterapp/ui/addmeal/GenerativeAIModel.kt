package com.example.caloriecounterapp.ui.addmeal

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class GenerativeAIModel {
    private val generativeModel = GenerativeModel(
        // The Gemini 1.5 models are versatile and work with most use cases
        modelName = "gemini-1.5-flash",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = com.example.caloriecounterapp.BuildConfig.apiKey
    )

    public suspend fun ProvideCalorieEstimation(inputString: String) : String
    {
        val prompt = "Estimate calories in food " + inputString + ". If you don't know exact details about food make some assumptions. Describe the answer and assumptions in one sentence"
        val response = generativeModel.generateContent(prompt)

        return response.text.toString();
    }

    public suspend fun ProvideCalorieEstimationFromPhoto(bitmap: Bitmap) : String
    {
        val response = generativeModel.generateContent(
            content {
                image(bitmap)
                text("Estimate calories in the food on the picture. If you don't know exact details about food make some assumptions. Describe the answer and assumptions in one sentence")
            }
        )

        return response.text.toString();
    }

}