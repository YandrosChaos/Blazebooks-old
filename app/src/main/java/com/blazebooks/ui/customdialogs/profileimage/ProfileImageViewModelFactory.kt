package com.blazebooks.ui.customdialogs.profileimage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.preferences.PreferenceProvider


@Suppress("UNCHECKED_CAST")
class ProfileImageViewModelFactory(
    private val prefs: PreferenceProvider
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileImageDialogViewModel(prefs) as T
    }

}