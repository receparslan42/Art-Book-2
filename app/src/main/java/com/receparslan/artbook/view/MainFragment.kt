package com.receparslan.artbook.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.receparslan.artbook.adapter.MainRecyclerAdapter
import com.receparslan.artbook.databinding.FragmentMainBinding
import com.receparslan.artbook.viewmodel.MainViewModel
import javax.inject.Inject

class MainFragment @Inject constructor(
    private val mainRecyclerAdapter: MainRecyclerAdapter
) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels() // Using activityViewModels to share ViewModel across fragments in the same activity

    //Using view binding to avoid findViewById
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    // Swipe to delete functionality
    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val selectedArt = mainRecyclerAdapter.arts[position]

            viewModel.deleteArt(selectedArt)

            Toast.makeText(requireContext(), "Art successfully deleted!", Toast.LENGTH_SHORT).show()
        }
    }

    // onCreateView is called to inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView with the adapter and layout manager
        binding.listRecyclerView.adapter = mainRecyclerAdapter
        binding.listRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up Floating Action Button to navigate to DetailFragment
        binding.fab.setOnClickListener { findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment()) }

        subscribeToObservers() // Observe the art list from the ViewModel and update the adapter

        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.listRecyclerView) // Attach swipe functionality to the RecyclerView
    }

    // Subscribe to LiveData observers to update the UI when data changes
    private fun subscribeToObservers() {
        viewModel.artList.observe(viewLifecycleOwner) { mainRecyclerAdapter.arts = it }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks by setting binding to null
    }
}