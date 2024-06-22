package com.example.caloriecounterapp.ui.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriecounterapp.MealModel
import com.example.caloriecounterapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerViewMeals = binding.recyclerViewMeals

        // Here, we have created new array list and added data to it
        val mealModelArrayList = ArrayList<MealModel>()
        mealModelArrayList.add(MealModel("Breakfast", "Oatmeal", 300))
        mealModelArrayList.add(MealModel("Lunch", "Burger", 500))
        mealModelArrayList.add(MealModel("Breakfast", "Oatmeal", 300))
        mealModelArrayList.add(MealModel("Lunch", "Burger", 500))
        mealModelArrayList.add(MealModel("Breakfast", "Oatmeal", 300))
        mealModelArrayList.add(MealModel("Lunch", "Burger", 500))
        mealModelArrayList.add(MealModel("Breakfast", "Oatmeal", 300))
        mealModelArrayList.add(MealModel("Lunch", "Burger", 500))
        mealModelArrayList.add(MealModel("Breakfast", "Oatmeal", 300))
        mealModelArrayList.add(MealModel("Lunch", "Burger", 500))
        mealModelArrayList.add(MealModel("Breakfast", "Oatmeal", 300))
        mealModelArrayList.add(MealModel("Lunch", "Burger", 500))
        mealModelArrayList.add(MealModel("Breakfast", "Oatmeal", 300))
        mealModelArrayList.add(MealModel("Lunch", "Burger", 500))

        // we are initializing our adapter class and passing our arraylist to it.
        val mealAdapter  = MealAdapter(inflater.context, mealModelArrayList)

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(inflater.context, LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerViewMeals.layoutManager = linearLayoutManager
        recyclerViewMeals.adapter = mealAdapter

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}