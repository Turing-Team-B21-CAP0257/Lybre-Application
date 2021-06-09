package com.b21.finalproject.smartlibraryapp.ui.home.ui.returnbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookWithDeadlineEntity

class ReturnBookViewModel(private val bookRepository: BookRepository) : ViewModel() {

    fun getAllBooks(sort: String): LiveData<List<BookEntity>> = bookRepository.getAllBooks(sort)

    fun getAllborrowBooksByRaw(sort: Int, userId: String): LiveData<List<BookWithDeadlineEntity>> = bookRepository.getAllBorrowBookByRaw(sort, userId)

    fun updateBorrowBook(returnBook: Int, bookId: String) = bookRepository.updateBorrowBook(returnBook, bookId)
}