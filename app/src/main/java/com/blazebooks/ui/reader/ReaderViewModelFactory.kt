package com.blazebooks.ui.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.preferences.PreferenceProvider

@Suppress("UNCHECKED_CAST")
class ReaderViewModelFactory (
    private val preferenceProvider: PreferenceProvider
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReaderViewModel(preferenceProvider) as T
    }

}