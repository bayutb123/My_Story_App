package com.bayutb.mystoryapp.injection

import android.content.Context
import com.bayutb.mystoryapp.api.ApiConfig
import com.bayutb.mystoryapp.data.StoryRepository

object Injection {
    fun provideRepository(token:String) : StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService, token)
    }
}