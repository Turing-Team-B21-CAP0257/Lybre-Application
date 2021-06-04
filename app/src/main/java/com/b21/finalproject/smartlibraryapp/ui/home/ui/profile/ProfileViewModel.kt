package com.b21.finalproject.smartlibraryapp.ui.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.UserEntity

class ProfileViewModel(private val bookRepository: BookRepository) : ViewModel() {
    fun getUserByUsername(username: String): LiveData<UserEntity> = bookRepository.getUserByUsername(username)
}