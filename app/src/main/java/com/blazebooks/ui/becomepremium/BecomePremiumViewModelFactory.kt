package com.blazebooks.ui.becomepremium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.PremiumRepository

@Suppress("UNCHECKED_CAST")
class BecomePremiumViewModelFactory(
    private val premiumRepo: PremiumRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BecomePremiumViewModel(premiumRepo) as T
    }

}