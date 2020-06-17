package com.blazebooks.data.repositories

import com.blazebooks.data.firebase.FirestoreDataBase
import com.blazebooks.data.models.User

class UsersRepository(
    private val db: FirestoreDataBase
) {

    fun getCurrentUser(id: String): User = db.getUserFromUsersCollection(id)

    fun insert(user: User) = db.insertUserIntoUsersCollection(user)

    fun delete(id: String) = db.deleteUserIntoUsersCollection(id)

    fun update(user: User) = db.updateUserIntoUsersCollection(user)

}