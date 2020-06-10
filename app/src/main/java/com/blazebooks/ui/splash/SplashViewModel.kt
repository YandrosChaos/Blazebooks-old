package com.blazebooks.ui.splash

import androidx.lifecycle.ViewModel
import com.blazebooks.BuildConfig

class SplashViewModel : ViewModel() {
    val appVersion: String = "Blazebooks - ${BuildConfig.VERSION_NAME}"
}