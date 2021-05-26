package com.b21.finalproject.smartlibraryapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.di.Injection
import com.b21.finalproject.smartlibraryapp.ui.home.ui.bookmark.BookmarkViewModel
import com.b21.finalproject.smartlibraryapp.ui.home.ui.books.menu.allBooks.AllBooksViewModel
import com.b21.finalproject.smartlibraryapp.ui.home.ui.home.HomeViewModel

class ViewModelFactory private constructor(private val bookRepository: BookRepository) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context)).apply { INSTANCE = this }
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(bookRepository) as T
            }
            modelClass.isAssignableFrom(AllBooksViewModel::class.java) -> {
                return AllBooksViewModel(bookRepository) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                return BookmarkViewModel(bookRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel Class : ${modelClass.name}")
        }
    }
}