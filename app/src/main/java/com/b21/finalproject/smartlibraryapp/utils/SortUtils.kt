package com.b21.finalproject.smartlibraryapp.utils

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import java.lang.StringBuilder

object SortUtils {

    const val RECOMMENDED = "recommended"
    const val RANDOM      = "random"
    const val MOST        = "most"
    const val RETURN_BOOK = 0

    fun getSortedQuery(filter: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM book_tb ")
        when (filter) {
            RECOMMENDED -> simpleQuery.append("ORDER BY RANDOM() LIMIT 1000")
            RANDOM      -> simpleQuery.append("ORDER BY RANDOM() LIMIT 1000")
            MOST        -> simpleQuery.append("ORDER BY rating DESC LIMIT 1000")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

    fun getBookByQuery(query: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM book_tb ")
        simpleQuery.append("WHERE book_title LIKE \'%$query%\' LIMIT 1000")
        Log.d("MYTAG", simpleQuery.toString())
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

    fun getBorrowBookByQuery(query: Int, userId: String): SimpleSQLiteQuery {
        val simpleQuery = "SELECT * FROM borrow_tb WHERE userId = ${userId.toInt()} AND returnBook = $query"
        return SimpleSQLiteQuery(simpleQuery)
    }
}