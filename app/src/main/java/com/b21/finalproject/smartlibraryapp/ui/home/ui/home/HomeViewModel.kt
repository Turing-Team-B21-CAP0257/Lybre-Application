package com.b21.finalproject.smartlibraryapp.ui.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Yossy Syahida"
    }
    val text: LiveData<String> = _text
}