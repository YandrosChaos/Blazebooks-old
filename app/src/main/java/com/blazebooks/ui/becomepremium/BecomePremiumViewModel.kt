package com.blazebooks.ui.becomepremium

import androidx.lifecycle.ViewModel
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.PremiumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BecomePremiumViewModel(
    private val premiumRepository: PremiumRepository,
    private val usersRepository: LoginRepository
) : ViewModel() {

    val user by lazy { usersRepository.currentUser() }

    suspend fun savePremiumUser() = withContext(Dispatchers.IO) {
        premiumRepository.savePremiumUid(user!!.uid)
    }

}