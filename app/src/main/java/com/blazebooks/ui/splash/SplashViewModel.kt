package com.blazebooks.ui.splash

import androidx.lifecycle.ViewModel
import com.blazebooks.BuildConfig

class SplashViewModel: ViewModel() {
    val appVersion: String = "Blazebooks - ${com.blazebooks.BuildConfig.VERSION_NAME}"
}