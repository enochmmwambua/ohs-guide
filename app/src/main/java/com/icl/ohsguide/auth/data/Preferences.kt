package com.icl.ohsguide.auth.data

import android.content.Context
import android.content.SharedPreferences
import com.icl.ohsguide.auth.data.Preferences.values.ACCESS_TOKEN
import com.icl.ohsguide.auth.data.Preferences.values.IS_LOGGED_IN

class Preferences(context: Context) {

    val prefs: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    object values {
        const val ACCESS_TOKEN = "Authtoken"
        const val IS_LOGGED_IN = "LoggedIn"
    }

    //Saves the token and sets the user as logged in.
    fun saveAuthToken(AuthToken: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, AuthToken)
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.apply()
    }


     //Fetches the saved token (Returns null if none exists)
    fun fetchAuthToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }


     //Checks if the user is currently logged in
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    //Used when logging out the user
    fun logoutUser() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}