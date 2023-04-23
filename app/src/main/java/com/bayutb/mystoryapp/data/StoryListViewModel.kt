package com.bayutb.mystoryapp.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bayutb.mystoryapp.api.ApiConfig
import com.bayutb.mystoryapp.api.StoryListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryListViewModel(application: Application) : AndroidViewModel(application) {
    val listStory = MutableLiveData<ArrayList<StoryList>>()

    fun fetchUsers(token: String) {
        ApiConfig.getApiService().fetchStories("Bearer $token", 50).enqueue(object : Callback<StoryListResponse> {
            override fun onResponse(
                call: Call<StoryListResponse>,
                response: Response<StoryListResponse>
            ) {
                if (response.isSuccessful) {
                    listStory.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                Toast.makeText(getApplication(), "Failed to retrieve stories", Toast.LENGTH_SHORT).show()
                Log.d("Failure: ", "${t.message}")
            }
        })
    }

    fun getListStory() : LiveData<ArrayList<StoryList>>{
        return listStory
    }
}