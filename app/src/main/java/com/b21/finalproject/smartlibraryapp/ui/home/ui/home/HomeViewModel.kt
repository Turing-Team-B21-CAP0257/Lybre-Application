package com.b21.finalproject.smartlibraryapp.ui.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.UserEntity

class HomeViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Yossy Syahida"
    }
    val text: LiveData<String> = _text

    fun getAllBooks(sort: String): LiveData<List<BookEntity>> =
        bookRepository.getAllBooks(sort)

    fun getRecommendedBooks(sort: String): LiveData<List<BookEntity>> =
        bookRepository.getRecommendedBooks(sort)

    fun getBookByQuery(query: String): LiveData<List<BookEntity>> =
        bookRepository.getBookByQuery(query)
}