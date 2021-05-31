package com.b21.finalproject.smartlibraryapp.ui.home.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.databinding.ActivitySettingsBinding
import com.b21.finalproject.smartlibraryapp.ui.auth.LoginFragment

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)

        settingsBinding.layoutLogout.tvLogoutTitle.setOnClickListener {
            val intent = Intent(this@SettingsActivity, LoginFragment::class.java)
            startActivity(intent)
        }

        settingsBinding.layoutHeaderChangeLanguage.tvChangeLanguageTitle.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
    }

}