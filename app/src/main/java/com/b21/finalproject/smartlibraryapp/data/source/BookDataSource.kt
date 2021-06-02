package com.b21.finalproject.smartlibraryapp.data.source

import androidx.lifecycle.LiveData
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BorrowBookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.FavoriteBookEntity

interface BookDataSource {

    fun getAllBooks(sort: String): LiveData<List<BookEntity>>

    fun getRecommendedBooks(sort: String): LiveData<List<BookEntity>>

    fun getBookByQuery(text: String): LiveData<List<BookEntity>>

    fun getBookById(bookId: Int): LiveData<BookEntity>

    fun insertBorrowBook(borrowBookEntity: BorrowBookEntity)

    fun insertFavoriteBook(favoriteBookEntity: FavoriteBookEntity)

    fun getAllFavoriteBook(userId: String): LiveData<List<BookEntity>>

    fun getAllBorrowBook(userId: String): LiveData<List<BookEntity>>

    fun getBookByTitle(text: String): List<BookEntity>

}