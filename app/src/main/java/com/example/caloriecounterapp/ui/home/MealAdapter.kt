package com.example.caloriecounterapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecounterapp.MealModel
import com.example.caloriecounterapp.R

class MealAdapter(private val context: Context, mealModelArrayList: ArrayList<MealModel>) :
    RecyclerView.Adapter<MealAdapter.ViewHolder>() {
    private val mealModelArrayList: ArrayList<MealModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.meal_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealAdapter.ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: MealModel = mealModelArrayList[position]
        holder.mealType.setText(model.mealType)
        holder.mealDescription.setText(model.mealDescription)
        holder.mealCalories.setText(model.mealCalories.toString())
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return mealModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealType : TextView = itemView.findViewById(R.id.textViewMealName)
        val mealDescription : TextView = itemView.findViewById(R.id.textViewMealDescription)
        val mealCalories : TextView = itemView.findViewById(R.id.textViewCalorieCount )
    }

    // Constructor
    init {
        this.mealModelArrayList = mealModelArrayList
    }
}
