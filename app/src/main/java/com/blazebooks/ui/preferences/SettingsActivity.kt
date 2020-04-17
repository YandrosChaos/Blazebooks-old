package com.blazebooks.ui.preferences

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import com.blazebooks.R
import com.blazebooks.ui.MainActivity
import com.blazebooks.ui.PreconfiguredActivity

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

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,getString(R.string.should_restart),Toast.LENGTH_LONG).show()
    }
}