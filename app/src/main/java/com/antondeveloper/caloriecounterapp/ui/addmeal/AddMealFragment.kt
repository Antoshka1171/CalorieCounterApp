package com.antondeveloper.caloriecounterapp.ui.addmeal

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
import com.antondeveloper.caloriecounterapp.MainViewModel
import com.antondeveloper.caloriecounterapp.MealModel
import com.antondeveloper.caloriecounterapp.R
import com.antondeveloper.caloriecounterapp.databinding.FragmentAddMealBinding
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

    internal fun savePressed(context: Context, ) {
        val mealType = binding.spinnerMealType.selectedItem

        mealModel.mealType = mealType.toString()

        val date = mainViewModel.date
        if(mainViewModel.mealsHashMap[date]==null)
            mainViewModel.mealsHashMap[date] =  ArrayList()

        mainViewModel.mealsHashMap[date]!!.add(mealModel)
        mainViewModel.writeToFile(context)

        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)

        controller.navigateUp()
    }

    internal suspend fun revision(){
        val result = aiModel.ProvideCalorieEstimationRevision(mealModel.mealDescription, binding.RevisionText.text.toString())
        binding.textViewResponse.text = result.first

        mealModel.mealDescription = result.first
        mealModel.mealCalories = result.second

        binding.RevisionText.text.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            photoBitmap = (data.extras?.get("data") as Bitmap)
            lifecycleScope.launch {

                val result =  aiModel.ProvideCalorieEstimationFromPhoto(photoBitmap!!)
                binding.textViewResponse.text = result.first
                mealModel.mealDescription = result.first
                mealModel.mealCalories = result.second

                binding.RevisionText.visibility = View.VISIBLE
                binding.RevisionButton.visibility = View.VISIBLE

                binding.RevisionButton.setOnClickListener() {
                    lifecycleScope.launch {
                        revision()
                    }
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel =
            ViewModelProvider(this).get(AddMealViewModel::class.java)

        _binding = FragmentAddMealBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.RevisionText.visibility = View.INVISIBLE
        binding.RevisionButton.visibility = View.INVISIBLE

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

                val description = editText.getText().toString();
                mealModel.mealDescription = description


                val result = aiModel.ProvideCalorieEstimationDescription(mealModel.mealDescription)
                mealModel.mealDescription = result.first
                mealModel.mealCalories = result.second

                binding.RevisionText.visibility = View.VISIBLE
                binding.RevisionButton.visibility = View.VISIBLE

                binding.RevisionButton.setOnClickListener() {
                    lifecycleScope.launch {
                        revision()
                    }
                }

                binding.textViewResponse.text = mealModel.mealDescription

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