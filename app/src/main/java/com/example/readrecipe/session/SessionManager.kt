package com.example.readrecipe.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("readrecipe_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "logged_in_user_id"
        private const val NO_USER = -1L
    }

    fun saveSession(userId: Long) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getLoggedInUserId(): Long = prefs.getLong(KEY_USER_ID, NO_USER)

    fun isLoggedIn(): Boolean = getLoggedInUserId() != NO_USER

    fun clearSession() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }
}
