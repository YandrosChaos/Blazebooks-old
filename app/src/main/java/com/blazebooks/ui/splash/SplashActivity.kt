package com.blazebooks.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.databinding.ActivitySplashBinding
import com.blazebooks.ui.auth.login.LoginActivity
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

    private lateinit var viewModel: SplashViewModel
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
        val binding: ActivitySplashBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        binding.viewModel = viewModel

        loadLanguageConfig()

        Handler().postDelayed({
            iv_splash.progress = 1f
            iv_splash.loop(false)
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
                startActivity(it)
            }
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

}
