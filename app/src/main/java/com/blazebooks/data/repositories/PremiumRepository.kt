package com.blazebooks.data.repositories

import com.blazebooks.data.firebase.FirestoreDataBase

class PremiumRepository(
    private val db: FirestoreDataBase
) {

    fun savePremiumUid(uid: String) = db.savePremiumUid(uid)

    fun deletePremiumUid(uid: String) = db.deletePremiumUid(uid)

    fun getPremiumUid(uid: String) = db.isPremium(uid)
}