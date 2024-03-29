package com.blazebooks.ui.settings

import androidx.lifecycle.ViewModel
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.AccountRepository

class SettingsActivityViewModel(
    private val prefs: PreferenceProvider,
    private val repository: AccountRepository
) : ViewModel() {

    fun saveLanguagePref(lan: String) = prefs.setLanguage(lan)

    fun updateUsername(name: String) = repository.updateUsername(name)

    fun updateUserEmail(email: String) = repository.updateEmail(email)

    fun updatePassword(pass: String) = repository.updatePassword(pass)


}