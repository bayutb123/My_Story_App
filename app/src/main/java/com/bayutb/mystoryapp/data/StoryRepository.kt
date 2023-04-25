package com.bayutb.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bayutb.mystoryapp.api.ApiService

class StoryRepository(private val apiService: ApiService, private val token: String) {
    fun fetchStories(): LiveData<PagingData<StoryList>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}