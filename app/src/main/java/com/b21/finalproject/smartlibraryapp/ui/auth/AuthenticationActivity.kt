package com.b21.finalproject.smartlibraryapp.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.b21.finalproject.smartlibraryapp.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set default light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Set View Pager and Tabs
        val authPagerAdapter = AuthPagerAdapter(this, supportFragmentManager)
        binding.viewPagerAuth.adapter = authPagerAdapter
        binding.tabsAuth.setupWithViewPager(binding.viewPagerAuth)
    }
}