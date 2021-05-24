package com.b21.finalproject.smartlibraryapp.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_tb")
data class UserEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    var userId: String,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "age")
    var age: String,

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "sex")
    var sex: String,

    @ColumnInfo(name = "address")
    var address: String,

    @ColumnInfo(name = "birthday")
    var birthday: String,

    @ColumnInfo(name = "full_name")
    var full_name: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String
)
