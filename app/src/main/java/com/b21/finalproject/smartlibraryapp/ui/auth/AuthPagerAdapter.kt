package com.b21.finalproject.smartlibraryapp.ui.auth

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.b21.finalproject.smartlibraryapp.R

class AuthPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private val TAB_TITLES = intArrayOf(R.string.login_title, R.string.register_title)
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> RegisterFragment()
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? = context.getString(TAB_TITLES[position])
}