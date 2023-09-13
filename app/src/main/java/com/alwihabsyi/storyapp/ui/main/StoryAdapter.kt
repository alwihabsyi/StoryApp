package com.alwihabsyi.storyapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alwihabsyi.storyapp.data.remote.ListStory
import com.alwihabsyi.storyapp.databinding.ItemLayoutBinding
import com.alwihabsyi.storyapp.utils.glide

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(private val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStory) {
            binding.tvItemName.text = item.name
            item.photoUrl?.let {
                binding.ivItemPhoto.glide(it)
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ListStory>() {
        override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    var onClick: ((ListStory) -> Unit)? = null

}