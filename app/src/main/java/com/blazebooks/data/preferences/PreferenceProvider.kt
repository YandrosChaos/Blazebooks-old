package com.blazebooks.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

//SHARED PREFERENCES KEYS
private const val LANGUAGE_SETTING_KEY = "language"
private const val GENRES_SETTING_KEY = "preferredGenres"
private const val READ_MODE_KEY = "readMode"
private const val SOUND_MODE_KEY = "soundMode"
private const val NEW_USERNAME_KEY = "usernamePref"
private const val NEW_PASSWD_KEY = "passwdPref"
private const val NEW_EMAIL_KEY = "emailPref"
private const val SELECTED_PROFILE_IMAGE_KEY = "selectedProfileImg"
private const val DELETE_ACCOUNT_KEY = "deleteAccount"
private const val LAST_BOOK_SELECTED_KEY = "lastBookSelected"

//DEFAULT PREFERENCES
private const val DEFAULT_LANGUAGE: String = "English"
private const val DEFAULT_USER_IMAGE = "https://cdn.pixabay.com/photo/2013/07/13/11/34/owl-158411_960_720.png"

/**
 * SharedPreferences Operations
 *
 * @author Victor Gonzalez
 */
class PreferenceProvider(
    context: Context
) {
    private val appContext = context.applicationContext
    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(
            appContext
        )

    fun clearAll() = preferences.edit().clear().apply()

    fun userImage(): String? {
        return preferences.getString(SELECTED_PROFILE_IMAGE_KEY, null)
    }

    fun saveUserImage(url: String) =
        preferences.edit().putString(SELECTED_PROFILE_IMAGE_KEY, url).apply()

    fun cleanUserImage() = preferences.edit().putString(SELECTED_PROFILE_IMAGE_KEY, DEFAULT_USER_IMAGE).apply()

    fun getLanguage(): String? {
        return preferences.getString(LANGUAGE_SETTING_KEY, DEFAULT_LANGUAGE)
    }

    fun setLanguage(lan: String) = preferences.edit().putString(LANGUAGE_SETTING_KEY, lan).apply()

    fun getLastBook(): String? {
        return preferences.getString(LAST_BOOK_SELECTED_KEY, null)
    }

    fun setLastBook(url: String) =
        preferences.edit().putString(LAST_BOOK_SELECTED_KEY, url).apply()

    fun getLightMode() = preferences.getBoolean(READ_MODE_KEY, false)

    fun setLastPage(key: String, currentPage: Int) = preferences.edit().putInt(key, currentPage).apply()

    fun getLastPage(key: String) = preferences.getInt(key, 1)

    fun setFontSize(key: String, fontSize: Int) = preferences.edit().putInt(key ,fontSize).apply()

    fun getFontSize(key: String) = preferences.getInt(key, 14)


}