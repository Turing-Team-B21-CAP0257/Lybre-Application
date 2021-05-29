package com.b21.finalproject.smartlibraryapp.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "book_tb")
data class BookEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo
    var bookId: String,

    @NonNull
    @ColumnInfo(name = "isbn")
    var ISBN: String,

    @ColumnInfo(name = "book_title")
    var book_title: String,

    @ColumnInfo(name = "book_author")
    var book_author: String,

    @ColumnInfo(name = "year_of_publication")
    var year_publication: String,

    @ColumnInfo(name = "publisher")
    var publisher: String,

    @ColumnInfo(name = "image_url_s")
    var imageUrl_s: String,

    @ColumnInfo(name = "image_url_m")
    var imageUrl_m: String,

    @ColumnInfo(name = "image_url_l")
    var imageUrl_l: String,

    @ColumnInfo(name = "rating")
    var rating: String
) : Parcelable
