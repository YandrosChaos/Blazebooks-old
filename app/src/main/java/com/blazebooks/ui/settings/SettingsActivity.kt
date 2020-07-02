package com.blazebooks.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.ui.settings.sharedpreferences.SharedPreferencesFragment
import com.blazebooks.util.*
import java.util.*

/**
 * Shows the preferences that user can change into the app.
 *
 * @see SharedPreferencesFragment
 * @author Victor Gonzalez
 */
class SettingsActivity : PreconfiguredActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        loadSettingsMainView()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
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
        toast(getString(R.string.should_restart))
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    /**
     * Preload the app language selected by the user. If none selected, then uses the phone
     * language.
     *
     * @see PreconfiguredActivity
     * @author  Victor Gonzalez
     * @author  Mounir Zbayr
     */
    private fun loadNewConfig() {
        when (sharedPreferences.getString(
            LANGUAGE_SETTING_KEY,
            DEFAULT_LANGUAGE
        )) {
            "Spanish" -> defaultLocale = Locale("es")
            "English" -> defaultLocale = Locale("en")
        }
    }//loadNewConfig

    private fun loadSettingsMainView() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right)
            .replace(
                R.id.settingsFrameLayout,
                SharedPreferencesFragment()
            ).commit()
    }
}