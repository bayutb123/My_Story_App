package com.bayutb.mystoryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bayutb.mystoryapp.databinding.ActivityStoryDetailBinding
import com.bumptech.glide.Glide

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    companion object{
        private const val I_NAME = "name"
        private const val I_DESC = "description"
        private const val I_PHOTOURL = "photoUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
    }

    private fun getIntentData() {
        binding.apply {
            tvDetailName.text = intent.getStringExtra(I_NAME)
            tvDetailDescription.text = intent.getStringExtra(I_DESC)
            Glide.with(applicationContext).load(intent.getStringExtra(I_PHOTOURL)).fitCenter().into(ivDetailStory)
        }
    }
}