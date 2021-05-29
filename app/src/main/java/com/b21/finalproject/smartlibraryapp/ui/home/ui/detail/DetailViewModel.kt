package com.b21.finalproject.smartlibraryapp.ui.home.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BorrowBookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.FavoriteBookEntity

class DetailViewModel(private val bookRepository: BookRepository) : ViewModel() {

    fun getBookById(bookId: Int): LiveData<BookEntity> = bookRepository.getBookById(bookId)

    fun getRecommendedBooks(sort: String): LiveData<List<BookEntity>> =
        bookRepository.getRecommendedBooks(sort)

    fun insertBorrowBook(borrowBookEntity: BorrowBookEntity) = bookRepository.insertBorrowBook(borrowBookEntity)

    fun insertFavoriteBook(favoriteBookEntity: FavoriteBookEntity) = bookRepository.insertFavoriteBook(favoriteBookEntity)
}