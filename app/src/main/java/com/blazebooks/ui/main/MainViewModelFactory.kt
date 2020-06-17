package com.blazebooks.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.LoginRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val repository: LoginRepository,
    private val preferences: PreferenceProvider
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository, preferences) as T
    }
}