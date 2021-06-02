package com.b21.finalproject.smartlibraryapp.ui.home.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity

class BookmarkViewModel(private val bookRepository: BookRepository) : ViewModel() {

    fun getAllFavoriteBook(userId: String): LiveData<List<BookEntity>> = bookRepository.getAllFavoriteBook(userId)

}