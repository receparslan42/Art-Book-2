package com.receparslan.artbook.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.receparslan.artbook.R
import com.receparslan.artbook.databinding.FragmentDetailBinding
import com.receparslan.artbook.model.Art
import com.receparslan.artbook.model.ImageUrls
import com.receparslan.artbook.viewmodel.MainViewModel
import javax.inject.Inject

class DetailFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment() {

    lateinit var viewModel: MainViewModel

    // Using view binding to avoid findViewById
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // onCreateView is called to inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelHolder: MainViewModel by activityViewModels() // Using activityViewModels to share ViewModel across fragments in the same activity
        viewModel = viewModelHolder

        // Set click listener for the save button to create an Art object and save it
        binding.saveButton.setOnClickListener {
            val art = Art(
                name = binding.nameEditText.text.toString(),
                artist = binding.artistEditText.text.toString(),
                year = binding.yearEditText.text.toString(),
                imageUrl = viewModel.selectedImageUrls.value?.webformatURL ?: ""
            )

            viewModel.makeArt(art)
        }

        // Set click listener for the image view to navigate to the search fragment
        binding.artImageView.setOnClickListener { findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToSearchFragment()) }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        ) // Handle back press to navigate back to the previous fragment

        subscribeToObservers() // Subscribe to LiveData observers to update UI based on data changes
    }

    // This function subscribes to LiveData observers to update the UI when data changes
    private fun subscribeToObservers() {
        viewModel.selectedImageUrls.observe(viewLifecycleOwner) { imageUrls ->
            if (imageUrls.webformatURL.isNotEmpty()) {
                binding.artImageView.setImageDrawable(null) // Clear previous image
                glide.load(imageUrls.webformatURL).into(binding.artImageView) // Load new image using Glide
            } else
                binding.artImageView.setImageResource(R.drawable.select_an_image) // Set default image if no URL is selected

        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status["field"]) {
                "name" -> {
                    binding.nameEditText.error = status["message"] // Show error message for name field
                    viewModel.resetStatus()
                    return@observe
                }

                "artist" -> {
                    binding.artistEditText.error = status["message"] // Show error message for artist field
                    viewModel.resetStatus()
                    return@observe
                }

                "year" -> {
                    binding.yearEditText.error = status["message"] // Show error message for year field
                    viewModel.resetStatus()
                    return@observe
                }

                "image" -> {
                    Toast.makeText(requireContext(), status["message"], Toast.LENGTH_LONG).show() // Show error message for image URL
                    viewModel.resetStatus()
                    return@observe
                }

                "success" -> {
                    Toast.makeText(requireContext(), status["message"], Toast.LENGTH_LONG).show() // Show success message
                    viewModel.resetStatus() // Reset status after showing message
                    viewModel.resetImageUrls() // Reset selected image URLs
                    viewModel.setSelectedImageUrls(ImageUrls("", "")) // Reset image view to default image
                    findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToMainFragment())
                    return@observe
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks by setting binding to null
    }
}