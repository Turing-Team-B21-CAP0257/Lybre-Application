package com.b21.finalproject.smartlibraryapp.ui.splashscreen

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.services.DataManagerService
import com.b21.finalproject.smartlibraryapp.ui.auth.AuthenticationActivity
import com.b21.finalproject.smartlibraryapp.ui.home.HomeActivity
import java.lang.ref.WeakReference

class SplashScreenActivity : AppCompatActivity(), HandlerCallBack {

    private lateinit var progressBar: ProgressBar

    private lateinit var mBoundService: Messenger
    private var mServiceBound: Boolean = false

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBoundService = Messenger(service)
            mServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mServiceBound = false
        }
    }

    private class IncomingHandler(callback: HandlerCallBack) : Handler(Looper.getMainLooper()) {

        private var weakCallBack: WeakReference<HandlerCallBack> = WeakReference(callback)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                DataManagerService.PREPARATION_MESSAGE -> weakCallBack.get()?.onPreparation()
                DataManagerService.UPDATE_MESSAGE -> {
                    val bundle = msg.data
                    val progress = bundle.getLong("KEY_PROGRESS")
                    weakCallBack.get()?.updateProgress(progress)
                }
                DataManagerService.SUCCESS_MESSAGE -> weakCallBack.get()?.loadSuccess()
                DataManagerService.FAILED_MESSAGE -> weakCallBack.get()?.loadFailed()
                DataManagerService.CANCEL_MESSAGE -> weakCallBack.get()?.loadCancel()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        progressBar = findViewById(R.id.progressBar)

        val mBoundServiceIntent = Intent(this@SplashScreenActivity, DataManagerService::class.java)
        val mActiviyMessenger = Messenger(IncomingHandler(this))
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER, mActiviyMessenger)

        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

    override fun onPreparation() {
        Toast.makeText(this, "Memulai memuat data", Toast.LENGTH_LONG).show()
        progressBar.visibility = View.VISIBLE
    }

    override fun updateProgress(progress: Long) {
        Log.d("PROGRESS", "updateProgress: $progress")
        progressBar.visibility = View.VISIBLE
    }

    override fun loadSuccess() {
        progressBar.visibility = View.GONE
        Toast.makeText(this, "BERHASIL", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@SplashScreenActivity, AuthenticationActivity::class.java))
        finish()
    }

    override fun loadFailed() {
       Toast.makeText(this, "GAGAL", Toast.LENGTH_LONG).show()
    }

    override fun loadCancel() {
        finish()
    }
}

private interface HandlerCallBack {
    fun onPreparation()

    fun updateProgress(progress: Long)

    fun loadSuccess()

    fun loadFailed()

    fun loadCancel()
}