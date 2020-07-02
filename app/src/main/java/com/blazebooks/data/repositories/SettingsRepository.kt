package com.blazebooks.data.repositories

import com.blazebooks.data.firebase.FirebaseSource

class SettingsRepository(
    private val firebase: FirebaseSource
) {
    fun updateUsername(name: String) = firebase.updateUserName(name)

    fun updatePhotoUri(uri: String?) = firebase.updatePhotoUri(uri)

    fun updateEmail(email: String) = firebase.updateEmail(email)

    fun updatePassword(passwd: String) = firebase.updatePassword(passwd)

    fun deleteAccount() = firebase.delete()
}