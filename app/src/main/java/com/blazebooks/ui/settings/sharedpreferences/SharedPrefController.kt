package com.blazebooks.ui.settings.sharedpreferences

import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedPrefController(
    private val repository: SettingsRepository,
    private val logRepo: LoginRepository
) {
    suspend fun deleteAccount() = withContext(Dispatchers.IO) { repository.deleteAccount() }

    suspend fun updateUsername(name: String) =
        withContext(Dispatchers.IO) { repository.updateUsername(name) }

    suspend fun updatePassword(pass: String) =
        withContext(Dispatchers.IO) { repository.updatePassword(pass) }

    suspend fun updateEmail(email: String) =
        withContext(Dispatchers.IO) { repository.updateEmail(email) }

    fun logout() = logRepo.logout()

}