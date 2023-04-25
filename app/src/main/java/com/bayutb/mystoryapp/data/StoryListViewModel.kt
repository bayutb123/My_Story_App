package com.bayutb.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bayutb.mystoryapp.injection.Injection

class StoryListViewModel(repository: StoryRepository) : ViewModel() {
    val storyList: LiveData<PagingData<StoryList>> = repository.fetchStories().cachedIn(viewModelScope)
}

class Factory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryListViewModel(Injection.provideRepository("Bearer $token")) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}