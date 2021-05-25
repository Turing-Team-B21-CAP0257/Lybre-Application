package com.b21.finalproject.smartlibraryapp.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.RatingEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.UserEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.room.LibraryDatabase
import com.b21.finalproject.smartlibraryapp.prefs.AppPreference
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.CoroutineContext

class DataManagerService : Service(), CoroutineScope {

    private val TAG = DataManagerService::class.java.simpleName
    private var mActivityMessenger: Messenger? = null

    companion object {
        const val PREPARATION_MESSAGE = 0
        const val UPDATE_MESSAGE = 1
        const val SUCCESS_MESSAGE = 2
        const val FAILED_MESSAGE = 3
        const val CANCEL_MESSAGE = 4
        const val ACTIVITY_HANDLER = "activity_handler"
        private const val MAX_PROGRESS = 100.0
    }

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate() {
        super.onCreate()
        job = Job()
        Log.d(TAG, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onBind(intent: Intent): IBinder {

        mActivityMessenger = intent.getParcelableExtra(ACTIVITY_HANDLER)

        loadDataAsync()

        return mActivityMessenger.let { it?.binder!! }

    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        job.cancel()
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind: ")
    }

    private fun loadDataAsync() {
        sendMessage(PREPARATION_MESSAGE)
        job = launch {
            val isInsertSuccess = async(Dispatchers.IO){
                getData()
            }
            if (isInsertSuccess.await()) {
                sendMessage(SUCCESS_MESSAGE)
            } else {
                sendMessage(FAILED_MESSAGE)
            }
        }
        job.start()
    }

    private fun sendMessage(messageStatus: Int) {
        val message = Message.obtain(null, messageStatus)
        try {
            mActivityMessenger?.send(message)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun getData(): Boolean {
        val databaseHelper = LibraryDatabase.getInstance(applicationContext)

        val appPreference = AppPreference(applicationContext)

        val firstRun = appPreference.firstRun as Boolean

        if (firstRun) {
//            val bookModels = preLoadRawBooks()
            val ratingModels = preLoadRawRatings()
//            val userModels = preLoadRawUsers()

            var progress = 30.0
            publishProgress(progress.toInt())
            val progressMaxInsert = 100.0
            val progressDiff = (progressMaxInsert - progress / ratingModels.size)

            var isInsertSucccess: Boolean

            try {
//                databaseHelper.beginTransaction()
//                databaseHelper.libraryDao().insertBookEntities(bookModels)
                databaseHelper.libraryDao().insertRatingEntities(ratingModels)
//                databaseHelper.libraryDao().insertUserEntities(userModels)
                for (i in 0..100) {
                    progress += progressDiff
                }
//                databaseHelper.setTransactionSuccessful()
                isInsertSucccess = true
                appPreference.firstRun = false
            } catch (e: Exception) {
                Log.e(TAG, "doInBackground: Exception")
                isInsertSucccess = false
            }
//            finally {
//                databaseHelper.endTransaction()
//            }

            publishProgress(MAX_PROGRESS.toInt())

            return isInsertSucccess
        } else {
            try {
                synchronized(this) {
                    publishProgress(50)
                    publishProgress(MAX_PROGRESS.toInt())
                    return true
                }
            } catch (e: Exception) {
                return false
            }
        }
    }

    private fun publishProgress(progress: Int) {
        try {
            val message = Message.obtain(null, UPDATE_MESSAGE)
            val bundle = Bundle()
            bundle.putLong("KEY_PROGRESS", progress.toLong())
            message.data = bundle
            mActivityMessenger?.send(message)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

//    private fun preLoadRawBooks(): ArrayList<BookEntity> {
//        val bookModels = ArrayList<BookEntity>()
//        var line: String?
//        val reader: BufferedReader
//        try {
//            val rawText = resources.openRawResource(R.raw.Books)
//
//            reader = BufferedReader(InputStreamReader(rawText))
//
//            reader.readLine()
//
//            do {
//                line = reader.readLine()
//                val splitstr = line.split(",").toTypedArray()
//
//                val bookModel = BookEntity(
//                    splitstr[0],
//                    splitstr[1],
//                    splitstr[2],
//                    splitstr[3],
//                    splitstr[4],
//                    splitstr[5],
//                    splitstr[6],
//                    splitstr[7]
//                )
//                bookModels.add(bookModel)
//            } while (line != null)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return bookModels
//    }

    private fun preLoadRawRatings(): ArrayList<RatingEntity> {
        val ratingModels = ArrayList<RatingEntity>()
        var line: String?
        val reader: BufferedReader
        try {
            val rawText = resources.openRawResource(R.raw.ratings)

            reader = BufferedReader(InputStreamReader(rawText))

            reader.readLine()

            do {
                line = reader.readLine()
                val splitstr = line.split(",").toTypedArray()

                val ratingModel = RatingEntity(
                    splitstr[0],
                    splitstr[1],
                    splitstr[2]
                )
                ratingModels.add(ratingModel)
            } while (line != null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ratingModels
    }

//    private fun preLoadRawUsers(): ArrayList<UserEntity> {
//        val userModels = ArrayList<UserEntity>()
//        var line: String?
//        val reader: BufferedReader
//        try {
//            val rawText = resources.openRawResource(R.raw.Users)
//
//            reader = BufferedReader(InputStreamReader(rawText))
//
//            reader.readLine()
//
//            do {
//                line = reader.readLine()
//                val splitstr = line.split(",").toTypedArray()
//
//                val userModel = UserEntity(
//                    splitstr[0],
//                    splitstr[1],
//                    splitstr[2],
//                    "Lorem ipsum",
//                    "Female",
//                    "Lorem ipsum",
//                    "2000/01/01",
//                    "Lorem ipsum",
//                    "Lorem ipsum",
//                    "LoremIpsum"
//                )
//                userModels.add(userModel)
//            } while (line != null)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return userModels
//    }
}