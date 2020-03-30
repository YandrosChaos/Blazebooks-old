package com.blazebooks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {
    //After completion of 2000 ms, the next activity will get started.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //this will bind your SplashActivity.class file with activity_splash.
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
                //invoke the MainActivity.
                startActivity(
                    Intent(
                        this@SplashActivity,
                        MainActivity::class.java
                    )
                )
            //the current activity will get finished.
            finish()
        }, Constant.SPLASH_SCREEN_TIME_OUT.toLong())
    }

}
