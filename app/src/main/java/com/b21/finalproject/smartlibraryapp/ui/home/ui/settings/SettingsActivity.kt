package com.b21.finalproject.smartlibraryapp.ui.home.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.databinding.ActivitySettingsBinding
import com.b21.finalproject.smartlibraryapp.ui.auth.AuthenticationActivity
import com.b21.finalproject.smartlibraryapp.ui.auth.LoginFragment

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)

        setSupportActionBar(settingsBinding.layoutHeaderSettings.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsBinding.layoutLogout.tvLogoutTitle.setOnClickListener {
            val intent = Intent(this@SettingsActivity, AuthenticationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        settingsBinding.layoutHeaderChangeLanguage.tvChangeLanguageTitle.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
    }

}