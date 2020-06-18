package com.blazebooks.ui.customdialogs.profileimage

import androidx.lifecycle.ViewModel
import com.blazebooks.data.preferences.PreferenceProvider

class ProfileImageDialogViewModel(
    private val prefs: PreferenceProvider
) : ViewModel() {

    fun storeSelectedImage(url: String) = prefs.saveUserImage(url)

    fun cleanSelectedImage() = prefs.cleanUserImage()

}