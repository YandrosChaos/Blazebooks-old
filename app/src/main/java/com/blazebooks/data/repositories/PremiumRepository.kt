package com.blazebooks.data.repositories

import com.blazebooks.data.firebase.FirebaseSource

class PremiumRepository(
    private val firebaseSource: FirebaseSource
) {

    fun savePremiumAccount() = firebaseSource.savePremiumAccount()

    fun deletePremiumAccount() = firebaseSource.deletePremiumAccount()

    fun getPremiumAccount() = firebaseSource.isPremiumAccount()
}