package com.blazebooks.ui.customdialogs.profileimage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.repositories.SettingsRepository


@Suppress("UNCHECKED_CAST")
class ProfileImageViewModelFactory(
    private val repository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileImageDialogViewModel(repository) as T
    }

}