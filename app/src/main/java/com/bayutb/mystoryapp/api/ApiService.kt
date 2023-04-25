package com.bayutb.mystoryapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    suspend fun fetchStories(
        @Header("Authorization") token: String,
        @Query("size") loadSize: Int,
        @Query("page") page : Int
    ): StoryListResponse

    @GET("stories")
    fun fetchStoriesLocation(
        @Header("Authorization") token: String,
        @Query("size") loadSize: Int,
        @Query("location") location :Int
    ): Call<StoryListResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") desc: RequestBody
    ) : Call<UploadResponse>
}