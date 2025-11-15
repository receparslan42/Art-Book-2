package com.receparslan.artbook.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.receparslan.artbook.adapter.SearchRecyclerAdapter
import com.receparslan.artbook.databinding.FragmentSearchBinding
import com.receparslan.artbook.util.Status
import com.receparslan.artbook.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment @Inject constructor(
    val searchRecyclerAdapter: SearchRecyclerAdapter
) : Fragment() {

    lateinit var viewModel: MainViewModel

    // Using view binding to avoid findViewById
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    // onCreateView is called to inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelHolder: MainViewModel by activityViewModels() // Using activityViewModels to share ViewModel across fragments in the same activity
        viewModel = viewModelHolder

        // Set up RecyclerView with the adapter and layout manager
        binding.searchRecyclerView.adapter = searchRecyclerAdapter
        binding.searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        searchRecyclerAdapter.setOnItemClickListener {
            viewModel.setSelectedImageUrls(it)
            findNavController().popBackStack()
        }

        var job: Job? = null // Job to handle debouncing of search input

        // Set up the search EditText to listen for text changes
        binding.searchEditText.addTextChangedListener { editable ->
            // If the text is empty, clear the image URLs
            if (editable.isNullOrEmpty()) {
                viewModel.resetImageUrls() // Clear the image URLs
            } else {
                // Search for images based on the text input
                job?.cancel() // Cancel any previous search job
                job = lifecycleScope.launch {
                    delay(1000)
                    viewModel.searchImage(editable.toString())
                }
                job.start()
            }

        }

        subscribeToObservers() // Observe the image URLs from the ViewModel
    }

    // This function subscribes to the ViewModel's LiveData to observe changes in image URLs
    private fun subscribeToObservers() {
        viewModel.imageUrls.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    searchRecyclerAdapter.urls = response.data ?: emptyList()
                    binding.loadingProgressBar.visibility = View.GONE
                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(), response.message ?: "", Toast.LENGTH_LONG).show()
                    binding.loadingProgressBar.visibility = View.GONE
                }

                Status.LOADING -> binding.loadingProgressBar.visibility = View.VISIBLE

                Status.NONE -> {
                    searchRecyclerAdapter.urls = emptyList()
                    binding.loadingProgressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks by setting binding to null
    }
}