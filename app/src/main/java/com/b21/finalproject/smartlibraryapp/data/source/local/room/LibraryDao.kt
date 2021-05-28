package com.b21.finalproject.smartlibraryapp.data.source.local.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.RatingEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.UserEntity

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookEntities(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRatingEntities(ratings: List<RatingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserEntities(users: List<UserEntity>)

    @RawQuery(observedEntities = [BookEntity::class])
    fun getAllbooks(query: SupportSQLiteQuery): List<BookEntity>

    @Query("SELECT * FROM rating_tb")
    fun getAllRatings(): List<RatingEntity>

    @Query("SELECT * FROM book_tb WHERE bookId = :bookId")
    fun getBookById(bookId: Int): BookEntity
}