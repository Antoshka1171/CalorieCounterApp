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
        //val prompt = inputString + "say only the number of calories"
        val response = generativeModel.generateContent(prompt)
        //ProvideCalorieEstimationNumber(response.text.toString())

        return response.text.toString();
    }

    public suspend fun ProvideCalorieEstimationNumber(inputString: String) : Int?
    {
        val prompt = "Estimate calories in food " + inputString + ". If you don't know exact details about food make some assumptions. provide the output in this format: X calories";

        //val prompt = inputString + ". Now say the integer number of calories."
        val response = generativeModel.generateContent(prompt);        //return response.text.toString();
        //println(response.text.toString())
        val delimiter = " "
        val values: List<String> = response.text.toString().split(" ")
        val numCal: Int = values[0].toInt()
        //println(numCal)
        return numCal;
    }


    public suspend fun ProvideCalorieEstimationFromPhoto(bitmap: Bitmap) : Pair<String, Int>
    {
        val response = generativeModel.generateContent(
            content {
                image(bitmap)
                text("Estimate calories in the food on the picture. If you don't know exact details about food make some assumptions. Describe the answer and assumptions in one sentence.")
            }
        )

        //ProvideCalorieEstimationNumber(response.text.toString())
        val description = response.text.toString()
        val calories = GetCaloriesUsingRegex(description)
        return Pair(description, calories)
    }

    internal fun GetCaloriesUsingRegex(text : String) : Int
    {
        val regex = Regex("""(\d+) calories""")

        val match = regex.find(text)
        if (match != null) {
            val calories = match.groups[1]?.value?.toIntOrNull()
            if (calories != null) {
                return calories;
            }
        }
        return 0;
    }
}