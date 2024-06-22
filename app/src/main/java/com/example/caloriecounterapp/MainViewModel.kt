package com.example.caloriecounterapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class MainViewModel : ViewModel()  {
    var mealList = ArrayList<MealModel>()

//    init {
//        mealList.add(MealModel("Breakfast", "Oatmeal", 300))
//        mealList.add(MealModel("Lunch", "Burger", 500))
//    }

    internal fun writeToFile(context: Context) {
        try {
            val fileOutputStream = context.openFileOutput("savedData.txt", Context.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(mealList)
            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }

    internal fun readFromFile(context: Context) {
        try {
            val fileInputStream = context.openFileInput("savedData.txt")
            val objectInputStream = ObjectInputStream(fileInputStream)
            mealList = objectInputStream.readObject() as ArrayList<MealModel>
            objectInputStream.close()
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

}