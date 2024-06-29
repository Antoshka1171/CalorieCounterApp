package com.antondeveloper.caloriecounterapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.Date
import java.util.HashMap


@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.time.toString())
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeString().toLong())
    }
}

class MainViewModel : ViewModel()  {
    var mealsHashMap = HashMap<LocalDate, ArrayList<MealModel>> ()
    var date = java.time.LocalDate.now().toKotlinLocalDate()

    internal fun writeToFile(context: Context) {
        try {
            val fileOutputStream = context.openFileOutput("savedData.txt", Context.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            val string = Json.encodeToString(mealsHashMap)
            objectOutputStream.writeUTF(string)
            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        } catch (e: SerializationException) {
            e.printStackTrace()
        }
    }

    internal fun readFromFile(context: Context) {
        try {
            val fileInputStream = context.openFileInput("savedData.txt")
            val objectInputStream = ObjectInputStream(fileInputStream)
            var jsonString = objectInputStream.readUTF()

            mealsHashMap = Json.decodeFromString(jsonString)

            objectInputStream.close()
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SerializationException) {
            e.printStackTrace()
        }

        val testDate = LocalDate(2024, 6, 10)
        if(mealsHashMap[testDate] == null) {
            mealsHashMap[testDate] = ArrayList()
            mealsHashMap[testDate]!!.add(MealModel("Breakfast", "egg", 100))
            mealsHashMap[testDate]!!.add(MealModel("lunch", "burger", 400))
        }
    }

    internal fun GetCurrentDateMeals() : ArrayList<MealModel>?
    {
        if (mealsHashMap[date] == null)
            mealsHashMap[date] = ArrayList()

        return mealsHashMap[date]
    }

}