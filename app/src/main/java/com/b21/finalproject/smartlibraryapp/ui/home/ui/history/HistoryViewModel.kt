package com.b21.finalproject.smartlibraryapp.ui.home.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookWithDeadlineEntity

class HistoryViewModel(private val bookRepository: BookRepository) : ViewModel() {

    fun getAllBorrowBook(userId: String): LiveData<List<BookWithDeadlineEntity>> = bookRepository.getAllBorrowBook(userId)

}