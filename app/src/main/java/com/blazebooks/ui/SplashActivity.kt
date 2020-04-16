package com.blazebooks.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import coil.Coil
import coil.ImageLoader
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * First activity. Load all data before the app start.
 *
 * @see MainActivity
 * @author Victor
 */
class SplashActivity : AppCompatActivity() {
    /**
     * After completion of Constants.SPLASH_SCREEN_TIME_OUT, executes the code
     * into Handler and the next activity will get started.
     *
     * @param savedInstanceState
     * @author Victor Gonzalez
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            //execute this code after the time indicated
            splashSpinKitView.visibility = View.GONE
            startActivity(
                Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        }, Constants.SPLASH_SCREEN_TIME_OUT.toLong())
    }

}
