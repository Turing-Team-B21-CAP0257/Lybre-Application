package com.b21.finalproject.smartlibraryapp.data.source

import com.b21.finalproject.smartlibraryapp.data.source.local.LocalDataSource
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.RatingEntity

class BookRepository private constructor(private val localDataSource: LocalDataSource) : BookDataSource{

    companion object {
        @Volatile
        private var INSTANCE: BookRepository? = null

        fun getInstance(mLocalDataSource: LocalDataSource): BookRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BookRepository(mLocalDataSource).apply { INSTANCE = this }
            }
    }

    override fun getAllBooks(): List<BookEntity> =
        localDataSource.getAllBooks()

    override fun getAllRatings(): List<RatingEntity> =
        localDataSource.getAllRatings()

    override fun getBookByIsbn(isbn: String): BookEntity =
        localDataSource.getBookByIsbn(isbn)

    override fun getBookByTitle(title: String): BookEntity =
        localDataSource.getBookByTitle(title)


}