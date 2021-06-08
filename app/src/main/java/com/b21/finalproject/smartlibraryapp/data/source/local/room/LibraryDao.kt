package com.b21.finalproject.smartlibraryapp.data.source.local.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.*

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookEntities(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserEntities(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNewUser(user: UserEntity)

    @Query("SELECT * FROM user_tb WHERE username = :username")
    fun checkUserByUsername(username: String): UserEntity

    @RawQuery(observedEntities = [BookEntity::class])
    fun getAllbooks(query: SupportSQLiteQuery): List<BookEntity>

    @Query("SELECT * FROM book_tb WHERE bookId = :bookId")
    fun getBookById(bookId: Int): BookEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBorrowBook(borrowBookEntity: BorrowBookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteBook(favoriteBookEntity: FavoriteBookEntity)

    @Query("SELECT * FROM favorite_tb WHERE userId = :userId")
    fun getAllFavoriteBook(userId: String): List<FavoriteBookEntity>

    @Query("SELECT * FROM favorite_tb WHERE bookId = :bookId")
    fun getFavoriteBookByBookId(bookId: String): FavoriteBookEntity

    @Query("DELETE FROM favorite_tb WHERE bookId = :bookId")
    fun deleteFavoriteBook(bookId: String)

    @Query("SELECT * FROM borrow_tb WHERE userId = :userId")
    fun getAllBorrowBook(userId: String): List<BorrowBookEntity>

    @RawQuery(observedEntities = [BorrowBookEntity::class])
    fun getAllborrowBookByRaw(query: SupportSQLiteQuery): List<BorrowBookEntity>

    @Query("UPDATE borrow_tb SET returnBook = :returnBook WHERE bookId = :bookId")
    fun updateBorrowBook(returnBook: Int, bookId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookWithDeadline(bookWithDeadlineEntity: BookWithDeadlineEntity)

    @Query("SELECT * FROM bookWithDeadline_tb WHERE user_id = :userId")
    fun getAllBookWithDeadline(userId: String): List<BookWithDeadlineEntity>

    @Query("SELECT * FROM user_tb WHERE username = :username")
    fun getUserByUsername(username: String): UserEntity
}