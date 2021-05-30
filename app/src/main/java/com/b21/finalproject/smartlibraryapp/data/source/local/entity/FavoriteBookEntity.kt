package com.b21.finalproject.smartlibraryapp.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tb")
data class FavoriteBookEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    val userId: String,
    val bookId: String,
    val favorite: Boolean
)
