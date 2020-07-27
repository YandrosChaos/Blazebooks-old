package com.blazebooks.ui.reader

import androidx.lifecycle.ViewModel
import com.blazebooks.data.preferences.PreferenceProvider

class BookStyleViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {

    fun setFontSize(key: String, fontSize: Int) = preferenceProvider.setFontSize(key, fontSize)
    fun getFontSize(key: String) = preferenceProvider.getFontSize(key)

}//class