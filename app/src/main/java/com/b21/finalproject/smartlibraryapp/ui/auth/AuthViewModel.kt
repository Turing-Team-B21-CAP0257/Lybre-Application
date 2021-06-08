package com.b21.finalproject.smartlibraryapp.ui.auth

import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.UserEntity

class AuthViewModel (private val bookRepository: BookRepository) : ViewModel() {
    fun getUserByUsername(username : String) = bookRepository.getUserByUsername(username)
    fun insertNewUser(user: UserEntity) = bookRepository.insertNewUser(user)
}