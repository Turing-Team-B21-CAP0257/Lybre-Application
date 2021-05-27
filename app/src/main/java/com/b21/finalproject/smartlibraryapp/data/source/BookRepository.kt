package com.b21.finalproject.smartlibraryapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.b21.finalproject.smartlibraryapp.data.source.local.LocalDataSource
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.RatingEntity
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class BookRepository private constructor(private val localDataSource: LocalDataSource) : BookDataSource{

    companion object {
        @Volatile
        private var INSTANCE: BookRepository? = null

        fun getInstance(mLocalDataSource: LocalDataSource): BookRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BookRepository(mLocalDataSource).apply { INSTANCE = this }
            }
    }

    override fun getAllBooks(sort: String): LiveData<List<BookEntity>> {
        val query = SortUtils.getSortedQuery(sort)
        val result = MutableLiveData<List<BookEntity>>()
        GlobalScope.async(Dispatchers.IO) {
            result.postValue(localDataSource.getAllBooks(query))
        }
        return result
    }

    override fun getAllRatings(): List<RatingEntity> =
        localDataSource.getAllRatings()

    override fun getBookByIsbn(isbn: String): BookEntity =
        localDataSource.getBookByIsbn(isbn)

    override fun getBookByTitle(title: String): BookEntity =
        localDataSource.getBookByTitle(title)


}