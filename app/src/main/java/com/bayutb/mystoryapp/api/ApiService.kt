package com.bayutb.mystoryapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String
    ):Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun authLogin(
        @Field("email") email: String,
        @Field("password") pass: String
    ) : Call<LoginResponse>

    @GET("stories")
    fun fetchStories(
        @Header("Authorization") token: String,
        @Query("size") loadSize: Int
    ): Call<StoryListResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") desc: RequestBody
    ) : Call<UploadResponse>
}