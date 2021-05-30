package com.b21.finalproject.smartlibraryapp.ui.home.ui.returnbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity

class ReturnBookViewModel(private val bookRepository: BookRepository) : ViewModel() {

    fun getAllBooks(sort: String): LiveData<List<BookEntity>> = bookRepository.getAllBooks(sort)

    fun getAllBorrowBooks(userId: String): LiveData<List<BookEntity>> = bookRepository.getAllBorrowBook(userId)
}