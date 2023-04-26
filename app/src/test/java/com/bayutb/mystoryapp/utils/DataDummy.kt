package com.bayutb.mystoryapp.utils

import com.bayutb.mystoryapp.api.StoryListResponse
import com.bayutb.mystoryapp.data.StoryList

object DataDummy {
    fun generateDummy(total :Int): StoryListResponse {
        val listStory = ArrayList<StoryList>()
        if (total > 0) {
            for (i in 1..total) {
                val data = StoryList(
                    id = "story-$i",
                    name = "name-$i",
                    description= "desc-$i",
                    photoUrl= "https://story-api.dicoding.dev/images/stories/photos-1682424636967_7R8gO_8m.jpg",
                    lat= -6.121435,
                    lon= 106.774124
                )
                listStory.add(data)
            }
        }
        return StoryListResponse(
            error = false,
            message = "Stories fetched successfully",
            items = listStory
        )
    }
}