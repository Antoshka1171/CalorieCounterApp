package com.example.caloriecounterapp.ui.addmeal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.caloriecounterapp.MainViewModel
import com.example.caloriecounterapp.MealModel
import com.example.caloriecounterapp.R
import com.example.caloriecounterapp.databinding.FragmentAddMealBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStreamWriter
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
    private val mainViewModel: MainViewModel by activityViewModels()

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

    internal fun savePressed(context: Context) {
        val mealType = binding.spinnerMealType.selectedItem
        println(mealType)

        mainViewModel.mealList.add(mealModel)
        mainViewModel.writeToFile(context)

        mealModel.mealType = mealType.toString()
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
        //controller.fi
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


                val result = aiModel.ProvideCalorieEstimationNumber(mealModel.mealDescription)

                mealModel.mealDescription = result.first
                mealModel.mealCalories = result.second

                binding.textViewResponse.text = mealModel.mealDescription.toString()
            }
        }

        val saveButton: ImageButton = binding.savemeal
        saveButton.setOnClickListener(){
            savePressed(root.context)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
        _binding = null
    }
}