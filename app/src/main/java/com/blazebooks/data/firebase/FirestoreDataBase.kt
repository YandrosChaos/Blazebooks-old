package com.blazebooks.data.firebase

import com.blazebooks.data.models.User
import com.blazebooks.util.FirestoreDatabaseException
import com.blazebooks.util.PremiumAccountNotFoundException
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable

private const val PREMIUM_ACCOUNTS_COLLECTION = "PremiumAccounts"
private const val PREMIUM_ACCOUNT_KEY = "account"
private const val USERS_COLLECTION = "Users"

/**
 * @author Victor Gonzalez
 */
class FirestoreDataBase {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun savePremiumUid(uid: String) = Completable.create { emitter ->
        db.collection(PREMIUM_ACCOUNTS_COLLECTION)
            .document(uid)
            .set(mapOf(PREMIUM_ACCOUNT_KEY to uid))
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun deletePremiumUid(uid: String) = Completable.create { emitter ->
        db.collection(PREMIUM_ACCOUNTS_COLLECTION)
            .document(uid)
            .delete()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun isPremium(uid: String) = Completable.create { emitter ->
        db.collection(PREMIUM_ACCOUNTS_COLLECTION)
            .document(uid)
            .get()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful && task.result != null && task.result!!.exists()) emitter.onComplete()
                    else emitter.onError(PremiumAccountNotFoundException("Premium account not found!"))
                }
            }
    }

    fun getUserFromUsersCollection(id: String): User {
        var user = User()

        db.collection(USERS_COLLECTION)
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
        db.collection(USERS_COLLECTION)
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
        db.collection(USERS_COLLECTION)
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
        db.collection(USERS_COLLECTION)
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