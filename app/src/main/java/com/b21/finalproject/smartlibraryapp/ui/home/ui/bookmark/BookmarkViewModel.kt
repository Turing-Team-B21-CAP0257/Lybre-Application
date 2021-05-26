package com.b21.finalproject.smartlibraryapp.ui.home.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b21.finalproject.smartlibraryapp.data.source.BookRepository

class BookmarkViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is bookmark Fragment"
    }
    val text: LiveData<String> = _text
}