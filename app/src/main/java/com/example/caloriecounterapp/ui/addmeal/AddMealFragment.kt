package com.example.caloriecounterapp.ui.addmeal

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.caloriecounterapp.MealModel
import com.example.caloriecounterapp.R
import com.example.caloriecounterapp.databinding.FragmentAddMealBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class AddMealFragment : Fragment() , CoroutineScope {

    companion object {
        fun newInstance() = AddMealFragment()
    }

    private var aiModel = GenerativeAIModel()
    private var _binding: FragmentAddMealBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AddMealViewModel by viewModels()

    var REQUEST_CODE = 101;
    fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    private var job: Job = Job()
    var mealModel = MealModel()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    internal var photoBitmap : Bitmap? = null;

    internal fun savePressed() {
        val mealType = binding.spinnerMealType.selectedItem
        println(mealType)

        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
        controller.navigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            photoBitmap = (data.extras?.get("data") as Bitmap)
            lifecycleScope.launch {
                val result =  aiModel.ProvideCalorieEstimationFromPhoto(photoBitmap!!)
                //binding.textViewResponse.text = result
                //val numberCal = aiModel.ProvideCalorieEstimationFromPhoto(photoBitmap!!)
                binding.textViewResponse.text = result.first
                mealModel.mealDescription = result.first
                mealModel.mealCalories = result.second
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //val result = binding.spinnerMealType.getSelectedView().text.toString()

        val viewModel =
            ViewModelProvider(this).get(AddMealViewModel::class.java)

        _binding = FragmentAddMealBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textViewResponse = binding.textViewResponse

        viewModel.mealDescription.observe(viewLifecycleOwner) {
            textViewResponse.text = it
        }

        binding.imageButton.setOnClickListener {
            capturePhoto()
        }

        val editText: EditText = binding.editTextMealDescription
        val descriptionButton: ImageButton = binding.generateDescriptionButon

        descriptionButton.setOnClickListener {
            lifecycleScope.launch {
                //val resultDescription = aiModel.ProvideCalorieEstimation(editText.getText().toString())
                //binding.textViewResponse.text = resultDescription

                mealModel.mealDescription = editText.getText().toString()

                val numberCal: Int? = aiModel.ProvideCalorieEstimationNumber(mealModel.mealDescription)
                mealModel.mealCalories = numberCal!!

                binding.textViewResponse.text = mealModel.mealCalories.toString()
            }
        }

        val saveButton: ImageButton = binding.savemeal
        saveButton.setOnClickListener(){
            savePressed()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
        _binding = null
    }
}