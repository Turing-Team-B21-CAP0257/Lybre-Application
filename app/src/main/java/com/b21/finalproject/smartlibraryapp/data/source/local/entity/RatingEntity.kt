package com.b21.finalproject.smartlibraryapp.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rating_tb")
data class RatingEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userId")
    var userId: String,

    @ColumnInfo(name = "isbn")
    var isbn: String,

    @ColumnInfo(name = "book_rating")
    var book_rating: String,

    @ColumnInfo(name = "bookId")
    var bookId: String
)
