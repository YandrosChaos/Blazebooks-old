package com.blazebooks.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.preference.PreferenceManager
import com.blazebooks.BuildConfig
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

/**
 * First activity. Preload data before the app start.
 *
 * @see PreconfiguredActivity
 * @see MainActivity
 * @author Victor
 */
class SplashActivity : PreconfiguredActivity() {

    private val timeOut = 6000
    private val spanishLanguage = "es"
    private val englishLanguage = "en"

    /**
     * After completion of Constants.SPLASH_SCREEN_TIME_OUT, executes the code
     * into Handler and the next activity will get started.
     *
     * @param savedInstanceState
     * @see MainActivity
     * @see loadLanguageConfig
     * @author Victor Gonzalez
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setUpAppVersion()
        loadLanguageConfig()

        Handler().postDelayed({
            //execute this code after the time indicated
            iv_splash.visibility = View.GONE
            startActivity(
                Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
            )
            overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
            finish()
        }, timeOut.toLong())
    }

    /**
     * Preload the app language selected by the user. If none selected, then uses the phone
     * language.
     *
     * @see PreconfiguredActivity
     * @see onCreate
     * @author  Victor Gonzalez
     */
    private fun loadLanguageConfig() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getString(
            Constants.LANGUAGE_SETTING_KEY,
            Constants.DEFAULT_LANGUAGE
        )) {
            "Spanish" -> defaultLocale = Locale(spanishLanguage)
            "English" -> defaultLocale = Locale(englishLanguage)
        }
    }

    /**
     * Shows version's name in content view. The version number
     * is stored in build.gradle (Module:app).
     *
     * @see onCreate
     * @author Victor Gonzalez
     */
    @SuppressLint("SetTextI18n")
    private fun setUpAppVersion() {
        splashVersionTV.text =
            "${resources.getString(R.string.app_name)} - ${BuildConfig.VERSION_NAME}"
    }

}
