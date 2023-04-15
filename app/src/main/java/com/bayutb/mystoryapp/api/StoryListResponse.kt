package com.bayutb.mystoryapp.api

import com.bayutb.mystoryapp.data.StoryList
import com.google.gson.annotations.SerializedName

data class StoryListResponse(

	@field:SerializedName("listStory")
	val items: ArrayList<StoryList>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
