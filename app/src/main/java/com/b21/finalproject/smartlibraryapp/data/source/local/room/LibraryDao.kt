package com.b21.finalproject.smartlibraryapp.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.RatingEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.UserEntity

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookEntities(books: LiveData<List<BookEntity>>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRatingEntities(ratings: LiveData<List<RatingEntity>>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserEntities(users: LiveData<List<UserEntity>>)

    @Query("SELECT * FROM book_tb")
    fun getAllbooks(): LiveData<List<BookEntity>>

    @Query("SELECT * FROM rating_tb")
    fun getAllRatings(): LiveData<List<RatingEntity>>

    @Query("SELECT * FROM book_tb WHERE ISBN = :isbn")
    fun getBookByisbn(isbn: String): LiveData<BookEntity>

    @Query("SELECT * FROM book_tb WHERE book_title = :bookTitle")
    fun getBookByTitle(bookTitle: String): LiveData<BookEntity>
}