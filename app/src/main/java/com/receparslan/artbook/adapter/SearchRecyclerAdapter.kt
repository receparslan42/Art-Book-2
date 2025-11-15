package com.receparslan.artbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.receparslan.artbook.databinding.ApiRecyclerRowBinding
import com.receparslan.artbook.model.ImageUrls
import javax.inject.Inject

class SearchRecyclerAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ImageUrls>() {
        override fun areItemsTheSame(oldItem: ImageUrls, newItem: ImageUrls): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: ImageUrls, newItem: ImageUrls): Boolean = oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, diffUtil)

    var urls: List<ImageUrls>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    private var onItemClickListener: ((ImageUrls) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(ApiRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) = holder.bind(urls[position])

    override fun getItemCount(): Int = urls.size

    fun setOnItemClickListener(listener: (ImageUrls) -> Unit) {
        onItemClickListener = listener
    }

    inner class SearchViewHolder(private val binding: ApiRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrls: ImageUrls) {
            glide.load(imageUrls.previewURL).into(binding.artImageView)

            binding.artImageView.setOnClickListener { onItemClickListener?.invoke(imageUrls) } // Set click listener to handle item clicks
        }
    }
}