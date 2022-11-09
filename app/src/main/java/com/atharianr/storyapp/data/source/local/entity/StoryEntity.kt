package com.atharianr.storyapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val photoUrl: String = "",
    val createdAt: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0
)