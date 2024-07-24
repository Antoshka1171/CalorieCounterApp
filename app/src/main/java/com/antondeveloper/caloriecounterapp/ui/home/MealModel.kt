package com.antondeveloper.caloriecounterapp.ui.home

import kotlinx.serialization.Serializable

@Serializable
data class MealModel (var _mealType:String = "", var _mealDescription:String = "", var  _mealCalories : Int = 0) {
    @Serializable
    var mealType = _mealType
    @Serializable
    var mealDescription = _mealDescription
    @Serializable
    var mealCalories = _mealCalories
}