package com.blazebooks.ui

import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import java.util.*

open class PreconfiguredActivity : AppCompatActivity(){

    companion object {
        var defaultLocale : Locale? = null
    }

    init {
        updateConfig(this)
    }

    private fun updateConfig(wrapper: ContextThemeWrapper){
        if(defaultLocale !=null){
            Locale.setDefault(defaultLocale)
            val configuration = Configuration()
            configuration.setLocale(defaultLocale)
            wrapper.applyOverrideConfiguration(configuration)
        }
    }
}