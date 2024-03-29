package com.blazebooks.ui.showbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.CatalogRepository
import com.blazebooks.data.repositories.StoredBooksRepository

@Suppress("UNCHECKED_CAST")
class ShowBookViewModelFactory(
    private val preferences: PreferenceProvider,
    private val storedBooksRepository: StoredBooksRepository,
    private val catalogRepository: CatalogRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShowBookViewModel(
            preferences,
            storedBooksRepository,
            catalogRepository
        ) as T

    }

}