package com.antondeveloper.caloriecounterapp.ui.addmeal

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.antondeveloper.caloriecounterapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import java.security.AccessController.getContext


class GenerativeAIModel {

    fun isOnline(context: Context): Boolean{
        var connection : Boolean= false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    connection = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    connection = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    connection = true
                }
            }
        }

        return connection

    }

    private val generativeModel = GenerativeModel(
        // The Gemini 1.5 models are versatile and work with most use cases
        modelName = "gemini-1.5-flash",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = BuildConfig.apiKey
    )


    public suspend fun ProvideCalorieEstimationDescription(inputString: String) : Pair<String, Int>
    {

        val prompt = "Estimate calories in food " + inputString + ". If you don't know exact details about food make some assumptions. Describe the answer and assumptions in one sentence.";

        //val prompt = inputString + ". Now say the integer number of calories."
        val response = generativeModel.generateContent(prompt);        //return response.text.toString();
        val description = response.text.toString()
        val calories = GetCaloriesUsingRegex(description)
        return Pair(description, calories)

    }


    public suspend fun ProvideCalorieEstimationFromPhoto(bitmap: Bitmap) : Pair<String, Int>
    {
        val response = generativeModel.generateContent(
            content {
                image(bitmap)
                text("Estimate calories in the food on the picture. If you don't know exact details about food make some assumptions. Describe the answer and assumptions in one sentence.")
            }
        )

        val description = response.text.toString()
        val calories = GetCaloriesUsingRegex(description)
        return Pair(description, calories)
    }

    public suspend fun ProvideCalorieEstimationRevision(originalEstimation: String, inputString: String) : Pair<String, Int>
    {
        val prompt = "This is the original calorie estimation: " + originalEstimation +
                ". Revise with this new information: " + inputString

        //val prompt = inputString + ". Now say the integer number of calories."
        val response = generativeModel.generateContent(prompt);        //return response.text.toString();
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