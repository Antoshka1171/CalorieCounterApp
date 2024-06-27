package com.example.caloriecounterapp.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriecounterapp.MainViewModel
import com.example.caloriecounterapp.databinding.FragmentHomeBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dayOfWeek = DayOfWeek.from(LocalDate.now()).toString()
        val result = dayOfWeek.first().toString() + dayOfWeek.substring(1).lowercase()

        binding.textViewDayOfWeek.text = result

        val localDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val formattedDate = localDate.format(formatter)

        binding.textViewDate.text = formattedDate.toString()

        val recyclerViewMeals = binding.recyclerViewMeals

        // we are initializing our adapter class and passing our arraylist to it.
        if(mainViewModel.mealsHashMap[mainViewModel.date] == null)
            mainViewModel.mealsHashMap[mainViewModel.date] = ArrayList()

        val mealAdapter  = MealAdapter(mainViewModel, inflater.context)
        mainViewModel.readFromFile(inflater.context)

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