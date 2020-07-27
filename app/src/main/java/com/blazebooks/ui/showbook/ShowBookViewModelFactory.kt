package com.blazebooks.ui.showbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.LikedBooksRepository
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.StoredBooksRepository

@Suppress("UNCHECKED_CAST")
class ShowBookViewModelFactory(
    private val preferences: PreferenceProvider,
    private val storedBooksRepository: StoredBooksRepository,
    private val likedBooksRepository: LikedBooksRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShowBookViewModel(
            preferences,
            storedBooksRepository,
            likedBooksRepository
        ) as T

    }

}