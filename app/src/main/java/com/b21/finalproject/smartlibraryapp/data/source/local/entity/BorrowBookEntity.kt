package com.b21.finalproject.smartlibraryapp.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "borrow_tb")
data class BorrowBookEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,
    val userId: String,
    val bookId: String,
    val borrowDayTime: String,
    val deadline: String,
    val aggreeRules: Boolean,
    val returnBook: Boolean = false
)