package com.receparslan.artbook.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.receparslan.artbook.adapter.MainRecyclerAdapter.MainViewHolder
import com.receparslan.artbook.databinding.MainRecyclerRowBinding
import com.receparslan.artbook.model.Art
import javax.inject.Inject

class MainRecyclerAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<MainViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Art>() {
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean = oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, diffUtil)

    var arts: List<Art>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    // Implement necessary methods for RecyclerView.Adapter
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(MainRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) = holder.bind(arts[position])

    override fun getItemCount(): Int = arts.size

    inner class MainViewHolder(private val binding: MainRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(art: Art) {
            glide.load(art.imageUrl).into(binding.artImageView)
            binding.artNameTextView.text = art.name
            binding.artistNameTextView.text = art.artist
            binding.artYearTextView.text = art.year
        }
    }
}