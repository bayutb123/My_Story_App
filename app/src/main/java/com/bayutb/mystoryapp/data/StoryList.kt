package com.bayutb.mystoryapp.data

import androidx.room.Entity

@Entity(tableName = "stories")
data class StoryList(
    val id: String,
    val name : String,
    val description : String,
    val photoUrl: String,
    val lat: Double,
    val lon: Double
) : java.io.Serializable