package com.b21.finalproject.smartlibraryapp.utils

import androidx.sqlite.db.SimpleSQLiteQuery
import java.lang.StringBuilder

object SortUtils {

    const val RECOMMENDED = "recommended"
    const val RANDOM      = "random"
    const val MOST        = "MOST"

    fun getSortedQuery(filter: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM book_tb ")
        when (filter) {
            RECOMMENDED -> simpleQuery.append("ORDER BY bookId ASC LIMIT 1000")
            RANDOM      -> simpleQuery.append("ORDER BY RANDOM() LIMIT 1000")
            MOST        -> simpleQuery.append("ORDER BY rating DESC LIMIT 1000")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}