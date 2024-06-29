package com.antondeveloper.caloriecounterapp.ui.addmeal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddMealViewModel : ViewModel() {
    private val _mealDescription = MutableLiveData<String>().apply {
        value = ""
    }
    private val _response = MutableLiveData<String>().apply {
        value = ""
    }

    val mealDescription: LiveData<String> = _mealDescription
    val response: LiveData<String> = _response
}