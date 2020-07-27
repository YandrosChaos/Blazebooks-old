package com.blazebooks.data.firebase

import com.blazebooks.data.models.Book
import com.blazebooks.util.DocumentNotFoundException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable


private const val COLLECTION = "FavBooks"
private const val SUB_COLLECTION = "likedBooks"

/**
 * @author Victor Gonzalez
 */
class FirestoreLikedBooks {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    fun isLiked(title: String) = Completable.create { emitter ->
        db.collection(COLLECTION)
            .document(firebaseAuth.currentUser.toString())
            .collection(SUB_COLLECTION)
            .document(title.replace(" ", ""))
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) emitter.onComplete()
            }
    }


    fun save(likedBook: Book) = Completable.create { emitter ->
        db.collection(COLLECTION)
            .document(firebaseAuth.currentUser.toString())
            .collection(SUB_COLLECTION)
            .document(likedBook.title.toString().replace(" ", ""))
            .set(likedBook)
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun delete(title: String) = Completable.create { emitter ->
        db.collection(COLLECTION)
            .document(firebaseAuth.currentUser.toString())
            .collection(SUB_COLLECTION)
            .document(title.replace(" ", ""))
            .delete()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }
}