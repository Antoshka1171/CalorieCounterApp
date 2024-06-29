package com.antondeveloper.caloriecounterapp

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