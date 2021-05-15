package com.b21.finalproject.smartlibraryapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.ui.auth.AuthenticationActivity

class SplashScreenActivity : AppCompatActivity() {

    private val TIME_SCREEN_END = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }, TIME_SCREEN_END)
    }
}