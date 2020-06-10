package com.blazebooks

import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * Activity that contains preconfigured settings.
 *
 * @author Victor González
 */
@Suppress("LeakingThis")
open class PreconfiguredActivity : AppCompatActivity(){

    companion object {
        var defaultLocale : Locale? = null
    }

    init {
        updateConfig(this)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun updateConfig(wrapper: ContextThemeWrapper){
        if(defaultLocale !=null){
            Locale.setDefault(defaultLocale)
            val configuration = Configuration()
            configuration.setLocale(defaultLocale)
            wrapper.applyOverrideConfiguration(configuration)
        }
    }
}