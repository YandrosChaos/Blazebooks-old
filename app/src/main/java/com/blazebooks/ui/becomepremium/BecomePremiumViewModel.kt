package com.blazebooks.ui.becomepremium

import androidx.lifecycle.ViewModel
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.PremiumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BecomePremiumViewModel(
    private val premiumRepository: PremiumRepository
) : ViewModel() {

    suspend fun savePremiumUser() = withContext(Dispatchers.IO) {
        premiumRepository.savePremiumAccount()
    }

}