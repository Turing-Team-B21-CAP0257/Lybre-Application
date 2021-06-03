package com.b21.finalproject.smartlibraryapp.ui.auth

import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository

class AuthViewModel (private val bookRepository: BookRepository) : ViewModel() {
    fun getUserByUsername(username : String) = bookRepository.getUserByUsername(username)
}