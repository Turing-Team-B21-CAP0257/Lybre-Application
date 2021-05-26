package com.b21.finalproject.smartlibraryapp.di

import android.content.Context
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.data.source.local.LocalDataSource
import com.b21.finalproject.smartlibraryapp.data.source.local.room.LibraryDatabase

object Injection {
    fun provideRepository(context: Context): BookRepository {

        val database = LibraryDatabase.getInstance(context)

        val localDataSource = LocalDataSource.getInstance(database.libraryDao())

        return BookRepository.getInstance(localDataSource)
    }
}