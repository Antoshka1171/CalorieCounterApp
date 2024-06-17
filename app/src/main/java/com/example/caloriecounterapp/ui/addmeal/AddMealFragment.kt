package com.example.caloriecounterapp.ui.addmeal

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.caloriecounterapp.R
import com.example.caloriecounterapp.databinding.FragmentAddMealBinding
import com.example.caloriecounterapp.databinding.FragmentGalleryBinding
import com.example.caloriecounterapp.databinding.FragmentSlideshowBinding
import com.example.caloriecounterapp.ui.slideshow.SlideshowViewModel

class AddMealFragment : Fragment() {

    companion object {
        fun newInstance() = AddMealFragment()
    }

    private var _binding: FragmentAddMealBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AddMealViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(AddMealViewModel::class.java)

        _binding = FragmentAddMealBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}