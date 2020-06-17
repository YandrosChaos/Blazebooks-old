package com.blazebooks.data.firebase

import com.blazebooks.data.models.User
import com.blazebooks.util.FirestoreDatabaseException
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable

class FirestoreDataBase {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getUserFromUsersCollection(id: String): User {
        var user = User()

        db.collection("Users")
            .document(id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        user = document.toObject(User::class.java)!!
                    } else {
                        throw FirestoreDatabaseException("The document is null!", task.exception)
                    }
                } else {
                    throw  FirestoreDatabaseException("Error getting the doc!", task.exception)
                }
            }

        return user
    }

    fun insertUserIntoUsersCollection(user: User) = Completable.create { emitter ->
        db.collection("Users")
            .document(user.userID)
            .set(user)
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun deleteUserIntoUsersCollection(id: String) = Completable.create { emitter ->
        db.collection("Users")
            .document(id)
            .delete()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun updateUserIntoUsersCollection(user: User) = Completable.create { emitter ->
        db.collection("Users")
            .document(user.userID)
            .set(user)
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }
}