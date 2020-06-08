package com.blazebooks.model

import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * Activity that contains preconfigured settings.
 *
 * @author Victor González
 */
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