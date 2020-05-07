package com.blazebooks.ui.preferences

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.MainActivity
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

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
    override fun onStop() {
        super.onStop()
        finish()
    }

    /**
     * Destroys this activity. Also loads the selected language.
     *
     * @see PreconfiguredActivity
     * @author Victor Gonzalez
     */
    override fun onDestroy() {
        super.onDestroy()
        loadLanguageConfig()
        Toast.makeText(this,getString(R.string.should_restart),Toast.LENGTH_LONG).show()
    }

    /**
     * Preload the app language selected by the user. If none selected, then uses the phone
     * language.
     *
     * @see PreconfiguredActivity
     * @author  Victor Gonzalez
     */
    private fun loadLanguageConfig() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getString(
            Constants.LANGUAGE_SETTING_KEY,
            Constants.DEFAULT_LANGUAGE
        )) {
            "Spanish" -> defaultLocale = Locale("es")
            "English" -> defaultLocale = Locale("en")
        }
    }
}