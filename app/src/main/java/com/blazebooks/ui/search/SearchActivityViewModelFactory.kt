package com.blazebooks.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.repositories.CatalogRepository
import com.blazebooks.data.repositories.StoredBooksRepository
import com.blazebooks.ui.search.control.SearchActivityViewModel

@Suppress("UNCHECKED_CAST")
class SearchActivityViewModelFactory(
    private val localBooksRepo: StoredBooksRepository,
    private val catalogRepository: CatalogRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchActivityViewModel(localBooksRepo, catalogRepository) as T
    }

}