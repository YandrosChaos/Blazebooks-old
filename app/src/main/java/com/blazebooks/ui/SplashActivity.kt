package com.blazebooks.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.preference.PreferenceManager
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

    private val timeOut = 4000

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
        Handler().postDelayed({
            //execute this code after the time indicated
            //set language
            loadLanguageConfig()
            splashSpinKitView.visibility = View.GONE
            startActivity(
                Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        }, timeOut.toLong())
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
