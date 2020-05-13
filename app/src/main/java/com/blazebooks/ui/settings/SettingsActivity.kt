package com.blazebooks.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.PreconfiguredActivity
import java.util.*

/**
 * Shows the preferences that user can change into the app.
 *
 * @see SettingsFragment
 * @author Victor Gonzalez
 */
class SettingsActivity : PreconfiguredActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Preload the app language selected by the user. If none selected, then uses the phone
     * language.
     *
     * @see PreconfiguredActivity
     * @author  Victor Gonzalez
     */
    private fun loadNewConfig() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getString(
            Constants.LANGUAGE_SETTING_KEY,
            Constants.DEFAULT_LANGUAGE
        )) {
            "Spanish" -> defaultLocale = Locale("es")
            "English" -> defaultLocale = Locale("en")
        }
    }

    /**
     * Returns to previous activity and sets custom animation transition.
     * Also loads the new config
     *
     * @author Victor Gonzalez
     */
    override fun onBackPressed() {
        super.onBackPressed()
        loadNewConfig()
        Toast.makeText(
            this,
            getString(R.string.should_restart),
            Toast.LENGTH_LONG
        ).show()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}