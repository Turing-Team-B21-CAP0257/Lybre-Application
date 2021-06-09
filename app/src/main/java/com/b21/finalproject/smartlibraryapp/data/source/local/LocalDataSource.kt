package com.b21.finalproject.smartlibraryapp.data.source.local

import android.util.Log
import androidx.sqlite.db.SupportSQLiteQuery
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.*
import com.b21.finalproject.smartlibraryapp.data.source.local.room.LibraryDao

class LocalDataSource private constructor(private val mLibraryDao: LibraryDao) {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(libraryDao: LibraryDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(libraryDao)
    }

    fun getAllBooks(sort: SupportSQLiteQuery): List<BookEntity> = mLibraryDao.getAllbooks(sort)

    fun getBookById(bookId: Int): BookEntity = mLibraryDao.getBookById(bookId)

    fun insertBorrowBook(borrowBookEntity: BorrowBookEntity) = mLibraryDao.insertBorrowBook(borrowBookEntity)

    fun insertFavoriteBook(favoriteBookEntity: FavoriteBookEntity) = mLibraryDao.insertFavoriteBook(favoriteBookEntity)

    fun getAllFavoriteBook(userId: String, onAllFavoriteBooksCallback: LoadFavoriteBooksCallback) {
        val favoriteBookEntity = mLibraryDao.getAllFavoriteBook(userId)
        onAllFavoriteBooksCallback.onAllFavoriteBooksReceived(favoriteBookEntity)
    }

    fun getFavoriteBookByBookId(bookId: String): FavoriteBookEntity = mLibraryDao.getFavoriteBookByBookId(bookId)

    fun deleteFavoriteBook(bookId: String) = mLibraryDao.deleteFavoriteBook(bookId)

    fun getAllBorrowBook(userId: String, onAllBorrowBooksCallback: LoadBorrowBooksCallback) {
        val borrowBookEntity = mLibraryDao.getAllBorrowBook(userId)
        onAllBorrowBooksCallback.onAllBorrowBooksReceived(borrowBookEntity)
    }

    fun getAllBookWithDeadline(userId: String): List<BookWithDeadlineEntity> = mLibraryDao.getAllBookWithDeadline(userId)

    fun insertBookWithDeadline(bookWithDeadlineEntity: BookWithDeadlineEntity) = mLibraryDao.insertBookWithDeadline(bookWithDeadlineEntity)

    fun getUserByUsername(username: String): UserEntity = mLibraryDao.getUserByUsername(username)

    fun updateBorrowBook(returnBook: Int, bookId: String) = mLibraryDao.updateBorrowBook(returnBook, bookId)

    fun getAllBorrowBookByRaw(sort: SupportSQLiteQuery, onAllBorrowBooksCallback: LoadBorrowBooksCallback) {
        val borrowBookEntity = mLibraryDao.getAllborrowBookByRaw(sort)
        onAllBorrowBooksCallback.onAllBorrowBooksReceived(borrowBookEntity)
    }

    fun insertNewUser(user: UserEntity) {
        mLibraryDao.insertNewUser(user)
    }

    fun getBookReccomendedById(bookIds: ArrayList<Int>, onBooksCallback: LoadBooksCallback) {
        val result = ArrayList<BookEntity>()
        for (i in bookIds.indices) {
            val getBook = mLibraryDao.getBookById(bookIds[i])
            val book = BookEntity(
                getBook.bookId,
                getBook.ISBN,
                getBook.book_title,
                getBook.book_author,
                getBook.year_publication,
                getBook.publisher,
                getBook.imageUrl_s,
                getBook.imageUrl_m,
                getBook.imageUrl_l,
                getBook.rating
            )
            result.add(book)
        }
        onBooksCallback.onBooksReceived(result)
    }

    interface LoadFavoriteBooksCallback {
        fun onAllFavoriteBooksReceived(favoriteBookEntity: List<FavoriteBookEntity>)
    }

    interface LoadBorrowBooksCallback {
        fun onAllBorrowBooksReceived(borrowBookEntity: List<BorrowBookEntity>)
    }

    interface LoadBooksCallback {
        fun onBooksReceived(bookEntity: List<BookEntity>)
    }

}