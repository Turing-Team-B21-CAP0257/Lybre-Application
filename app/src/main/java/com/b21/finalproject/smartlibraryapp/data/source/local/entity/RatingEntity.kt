package com.b21.finalproject.smartlibraryapp.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rating_tb")
data class RatingEntity(
    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "user_id")
    var userId: String,

    @ColumnInfo(name = "isbn")
    var ISBN: String,

    @ColumnInfo(name = "book_rating")
    var book_rating: String
)