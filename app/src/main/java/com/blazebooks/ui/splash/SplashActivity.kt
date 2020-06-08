package com.blazebooks.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import com.blazebooks.BuildConfig
import com.blazebooks.R
import com.blazebooks.model.PreconfiguredActivity
import com.blazebooks.ui.login.LoginActivity
import com.blazebooks.util.DEFAULT_LANGUAGE
import com.blazebooks.util.LANGUAGE_SETTING_KEY
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

/**
 * First activity. Preload data before the app start.
 *
 * @see PreconfiguredActivity
 * @see LoginActivity
 * @author Victor
 */
class SplashActivity : PreconfiguredActivity() {

    private val timeOut = 4000
    private val spanishLanguage = "es"
    private val englishLanguage = "en"

    /**
     * After completion of Constants.SPLASH_SCREEN_TIME_OUT, executes the code
     * into Handler and the next activity will get started.
     *
     * @param savedInstanceState
     * @see LoginActivity
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
            startActivity(
                Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
            )
            overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)

            //stop the loop of the animation
            iv_splash.progress = 1f
            iv_splash.loop(false)
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
            LANGUAGE_SETTING_KEY,
            DEFAULT_LANGUAGE
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
