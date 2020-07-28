package com.blazebooks.ui.customdialogs.profileimage

import androidx.lifecycle.ViewModel
import com.blazebooks.data.repositories.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileImageDialogViewModel(
    private val repository: AccountRepository
) : ViewModel() {

    suspend fun updatePhotoUri(url: String?) =
        withContext(Dispatchers.IO) { repository.updatePhotoUri(url) }

}