package com.b21.finalproject.smartlibraryapp.data.source.local

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

    interface LoadFavoriteBooksCallback {
        fun onAllFavoriteBooksReceived(favoriteBookEntity: List<FavoriteBookEntity>)
    }

    interface LoadBorrowBooksCallback {
        fun onAllBorrowBooksReceived(borrowBookEntity: List<BorrowBookEntity>)
    }
}