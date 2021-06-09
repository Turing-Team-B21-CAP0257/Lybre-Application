package com.b21.finalproject.smartlibraryapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository
import com.b21.finalproject.smartlibraryapp.di.Injection
import com.b21.finalproject.smartlibraryapp.ui.auth.AuthViewModel
import com.b21.finalproject.smartlibraryapp.ui.home.ui.bookmark.BookmarkViewModel
import com.b21.finalproject.smartlibraryapp.ui.home.ui.detail.DetailViewModel
import com.b21.finalproject.smartlibraryapp.ui.home.ui.history.HistoryViewModel
import com.b21.finalproject.smartlibraryapp.ui.home.ui.home.HomeViewModel
import com.b21.finalproject.smartlibraryapp.ui.home.ui.profile.ProfileViewModel
import com.b21.finalproject.smartlibraryapp.ui.home.ui.returnbook.ReturnBookViewModel

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
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                return BookmarkViewModel(bookRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(bookRepository) as T
            }
            modelClass.isAssignableFrom(ReturnBookViewModel::class.java) -> {
                return ReturnBookViewModel(bookRepository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                return AuthViewModel(bookRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                return HistoryViewModel(bookRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                return ProfileViewModel(bookRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel Class : ${modelClass.name}")
        }
    }
}