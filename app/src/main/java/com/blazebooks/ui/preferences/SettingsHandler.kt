package com.blazebooks.ui.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.blazebooks.R

/**
 * @see SettingsActivity
 * @author Victor Gonzalez
 */
class SettingsHandler(private val context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = prefs.edit()

    //user Keys
    private val KEY_APP_THEME = "appTheme"
    private val KEY_LANGUAGE = "language"

    //book keys
    private val KEY_PREFERED_GENRES = "preferedGenres"
    private val KEY_READ_MODE = "readMode"

    fun configSettings() {

        setAppTheme(prefs.getString(KEY_APP_THEME, "AppTheme").toString())

        prefs.getString(KEY_LANGUAGE, "spanish")
        prefs.getStringSet(KEY_PREFERED_GENRES, mutableSetOf())
        prefs.getBoolean(KEY_READ_MODE, false)
    }

    fun setAllSettings() {
        editor.putBoolean("", true)
        editor.apply()
    }

    private fun setAppTheme(theme : String){
        when(theme){
            "First Theme" -> context.setTheme(R.style.AppTheme)
            "Second Theme" -> context.setTheme(R.style.LoginPage)
        }
    }


}