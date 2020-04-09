package com.blazebooks.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    //After completion of 2000 ms, the next activity will get started.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //execute this code after the time indicated
        Handler().postDelayed({
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
