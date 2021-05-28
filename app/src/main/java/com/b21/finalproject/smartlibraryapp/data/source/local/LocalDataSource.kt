package com.b21.finalproject.smartlibraryapp.data.source.local

import androidx.sqlite.db.SupportSQLiteQuery
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.RatingEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.room.LibraryDao

class LocalDataSource private constructor(private val mLibraryDao: LibraryDao) {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(libraryDao: LibraryDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(libraryDao)
    }

    fun getAllBooks(sort: SupportSQLiteQuery): List<BookEntity> = mLibraryDao.getAllbooks(sort)

    fun getAllRatings(): List<RatingEntity> = mLibraryDao.getAllRatings()

    fun getBookByIsbn(isbn: String): BookEntity = mLibraryDao.getBookByisbn(isbn)

    fun getBookByTitle(title: String): BookEntity = mLibraryDao.getBookByTitle(title)

}