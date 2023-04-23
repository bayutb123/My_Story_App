package com.bayutb.mystoryapp.api

import android.content.Context
import android.content.SharedPreferences
import com.bayutb.mystoryapp.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun saveAuth(token: String, userId: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, userId)
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }

    fun checkAuth() :String? {
        return prefs.getString(USER_TOKEN,null)
    }

    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun clearAuth() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }
}