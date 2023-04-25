package com.bayutb.mystoryapp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bayutb.mystoryapp.databinding.CardStoryBinding
import com.bumptech.glide.Glide

class StoryListAdapter : PagingDataAdapter<StoryList, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallBack: OnItemClickCallBack?= null

    inner class ViewHolder(private val binding: CardStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryList) {
            binding.root.setOnClickListener {
                onItemClickCallBack?.onItemClicked(data)
            }

            binding.apply {
                cardTvName.text = data.name
                Glide.with(itemView).load(data.photoUrl).centerCrop().into(cardIvStoryImage)
            }
        }
    }

    fun setOnItemClickCallback (onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =CardStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: StoryList)
    }

    companion object {
        val DIFF_CALLBACK = object : androidx.recyclerview.widget.DiffUtil.ItemCallback<StoryList>() {
            override fun areItemsTheSame(oldItem: StoryList, newItem: StoryList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryList, newItem: StoryList): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}