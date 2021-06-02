package com.b21.finalproject.smartlibraryapp.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AppPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "LibraryPref"
        private const val PREFS_LOGIN = "LoginPref"
        private const val PREFS_USERID = "UserPref"
        private const val PREFS_USERNAME = "UserPref"
        private const val APP_FIRST_RUN = "app_first_run"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var firstRun: Boolean?
        get() = prefs.getBoolean(APP_FIRST_RUN, true)
        set(input) {
            prefs.edit {
                putBoolean(APP_FIRST_RUN, input as Boolean)
            }
        }

    var isLogin: Boolean?
        get() = prefs.getBoolean(PREFS_LOGIN, false)
        set(value) {
            prefs.edit {
                putBoolean(PREFS_LOGIN, value as Boolean)
            }
        }

    var userId: String?
        get() = prefs.getString(PREFS_USERID, "1")
        set(value) {
            prefs.edit {
                putString(PREFS_USERID, value)
            }
        }

    var username: String?
        get() = prefs.getString(PREFS_USERNAME, "loremipsum")
        set(value) {
            prefs.edit {
                putString(PREFS_USERNAME, value)
            }
        }
}