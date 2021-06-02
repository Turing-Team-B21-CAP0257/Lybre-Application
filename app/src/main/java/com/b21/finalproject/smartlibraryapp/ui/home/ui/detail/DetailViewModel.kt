package com.b21.finalproject.smartlibraryapp.ui.home.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BorrowBookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.FavoriteBookEntity
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class DetailViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private lateinit var resultBook: ArrayList<String>

    fun setResultBook(result: ArrayList<String>) {
        resultBook = result
    }

    fun getBookById(bookId: Int): LiveData<BookEntity> = bookRepository.getBookById(bookId)

    fun getRecommendedBooks(sort: String): LiveData<List<BookEntity>> =
        bookRepository.getRecommendedBooks(sort)

    fun insertBorrowBook(borrowBookEntity: BorrowBookEntity) =
        bookRepository.insertBorrowBook(borrowBookEntity)

    fun insertFavoriteBook(favoriteBookEntity: FavoriteBookEntity) =
        bookRepository.insertFavoriteBook(favoriteBookEntity)

    fun getFavoriteBookByBookId(bookId: String): LiveData<FavoriteBookEntity> = bookRepository.getFavoriteBookById(bookId)

    fun deleteFavoriteBook(bookId: String) = bookRepository.deleteFavoriteBook(bookId)

    fun getBookByTitle(): LiveData<List<BookEntity>> {
        var notEmpty: Boolean
        val result = bookRepository.getBookByQuery(resultBook[0] + " " + resultBook[1])
        if (result.value != null) notEmpty = false else notEmpty = true
        if (!notEmpty) {
            Log.d("kondisi", "true")
            return result
        } else {
            Log.d("kondisi", "false")
            return bookRepository.getBookByQuery(resultBook[0])
        }
    }
}