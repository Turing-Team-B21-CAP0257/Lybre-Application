package com.b21.finalproject.smartlibraryapp.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.RatingEntity
import com.b21.finalproject.smartlibraryapp.vo.Resource

interface BookDataSource {

    fun getAllBooks(): List<BookEntity>

    fun getAllRatings(): List<RatingEntity>

    fun getBookByIsbn(isbn: String): BookEntity

    fun getBookByTitle(title: String): BookEntity

}