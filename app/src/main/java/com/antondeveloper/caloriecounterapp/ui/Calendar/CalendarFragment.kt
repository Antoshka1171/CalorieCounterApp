package com.antondeveloper.caloriecounterapp.ui.Calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.antondeveloper.caloriecounterapp.MainViewModel
import com.antondeveloper.caloriecounterapp.databinding.FragmentCalendarBinding
import kotlinx.datetime.LocalDate
import java.time.DayOfWeek


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(com.antondeveloper.caloriecounterapp.ui.Calendar.CalednarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.calendarView2.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            val localDate = LocalDate(year, month + 1, dayOfMonth)

            val totalCalories = 0
            if(mainViewModel.mealsHashMap[localDate]!=null) {
                val totalCaloriesForDate = mainViewModel.mealsHashMap[localDate]!!.sumBy { it.mealCalories }
                val dayOfWeek = DayOfWeek.from(java.time.LocalDate.now()).toString()
                val result = dayOfWeek.first().toString() + dayOfWeek.substring(1).lowercase()
                binding.textView4.text = result + ": " + totalCaloriesForDate + " calories"
            } else{
                binding.textView4.text = "No information for this day"
            }



        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}