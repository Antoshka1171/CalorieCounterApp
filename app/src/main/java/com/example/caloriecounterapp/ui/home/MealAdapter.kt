package com.example.caloriecounterapp.ui.home

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecounterapp.MainViewModel
import com.example.caloriecounterapp.MealModel
import com.example.caloriecounterapp.R

class MealAdapter(private val mainViewModel: MainViewModel, private val context: Context) :
    RecyclerView.Adapter<MealAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.meal_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealAdapter.ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: MealModel = mainViewModel.mealsHashMap[mainViewModel.date]!![position]
        holder.mealType.setText(model.mealType)
        holder.mealDescription.setText(model.mealDescription)
        holder.mealCalories.setText(model.mealCalories.toString())

        holder.deleteButton.setOnClickListener(){
            mainViewModel.mealsHashMap[mainViewModel.date]!!.removeAt(position)
            notifyItemRemoved(position)
            mainViewModel.writeToFile(context)
        }
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return mainViewModel.mealsHashMap[mainViewModel.date]!!.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealType : TextView = itemView.findViewById(R.id.textViewMealName)
        val mealDescription : TextView = itemView.findViewById(R.id.textViewMealDescription)
        val mealCalories : TextView = itemView.findViewById(R.id.textViewCalorieCount)
        val deleteButton : ImageView = itemView.findViewById(R.id.deleteButton)
    }
}
