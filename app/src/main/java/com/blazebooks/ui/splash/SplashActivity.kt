package com.blazebooks.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.databinding.ActivitySplashBinding
import com.blazebooks.ui.auth.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

private const val ES_LAN = "es"
private const val EN_LAN = "en"
private const val TIME_OUT = 4000L

/**
 * First activity. Preload data before the app start.
 *
 * @see PreconfiguredActivity
 * @see LoginActivity
 * @author Victor
 */
class SplashActivity : PreconfiguredActivity() {

    private lateinit var viewModel: SplashViewModel

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
        }, TIME_OUT)
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
        when (PreferenceProvider(this).getLanguage()) {
            "Spanish" -> defaultLocale = Locale(ES_LAN)
            "English" -> defaultLocale = Locale(EN_LAN)
        }
    }

}
