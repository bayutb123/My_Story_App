package com.bayutb.mystoryapp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bayutb.mystoryapp.MapsActivity
import com.bayutb.mystoryapp.databinding.CardStoryBinding
import com.bumptech.glide.Glide

class StoryListAdapter : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {
    private var listStory = ArrayList<StoryList>()
    private var onItemClickCallBack: OnItemClickCallBack?= null

    inner class ViewHolder(private val binding: CardStoryBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    fun updateList(newPersonList : ArrayList<StoryList>) {
        val diffUtil = DiffUtil(listStory, newPersonList)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        listStory = newPersonList
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: StoryList)

    }

}