package com.blazebooks.data.firebase

import android.net.Uri
import com.blazebooks.data.models.Book
import com.blazebooks.data.models.Chapter
import com.blazebooks.util.DEFAULT_PROFILE_IMAGE
import com.blazebooks.util.PremiumAccountNotFoundException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest.Builder
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable

private const val FAV_BOOKS_COLLECTION = "FavBooks"
private const val FAV_BOOKS_SUBCOLLECTION = "likedBooks"
private const val PREMIUM_ACCOUNTS_COLLECTION = "PremiumAccounts"
private const val PREMIUM_ACCOUNT_KEY = "account"

/**
 * Completable es una clase de RxJava que permite obtener una indicación cuando
 * está completado o cuando a fallado. Esto es perfecto porque la autenticacion es una
 * operacion de red, y así podremos saber si se ha completado correctamente o no.
 *
 * Usamos el emitter para indicar si el task se ha completado.
 *
 * @author Victor Gonzalez
 */
class FirebaseSource {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun login(email: String, passwd: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) emitter.onComplete()
                else emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, passwd: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) emitter.onComplete()
                else emitter.onError(it.exception!!)
            }
        }
    }

    fun updateUserName(name: String) = Completable.create { emitter ->
        val profileUpdates = Builder().setDisplayName(name)
            .build()

        firebaseAuth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun updatePhotoUri(uri: String?) = Completable.create { emitter ->
        var newImage = uri
        val user = currentFirebaseUser()

        if (newImage.isNullOrEmpty()) {
            newImage = DEFAULT_PROFILE_IMAGE
        }

        val profileUpdates = Builder()
            .setDisplayName(user!!.displayName)
            .setPhotoUri(Uri.parse(newImage))
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }

    }

    fun updateEmail(email: String) = Completable.create { emitter ->
        firebaseAuth.currentUser?.updateEmail(email)
            ?.addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun updatePassword(passwd: String) = Completable.create { emitter ->
        firebaseAuth.currentUser?.updatePassword(passwd)
            ?.addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentFirebaseUser() = firebaseAuth.currentUser

    fun loginWithGoogle(account: GoogleSignInAccount) = Completable.create { emitter ->
        firebaseAuth.signInWithCredential(
            GoogleAuthProvider.getCredential(
                account.idToken,
                null
            )
        ).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) emitter.onComplete()
                else emitter.onError(it.exception!!)
            }
        }
    }

    fun sendPasswordReset(email: String) = Completable.create { emitter ->
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) emitter.onComplete()
                else emitter.onError(it.exception!!)
            }
        }
    }

    fun deleteAccount() = Completable.create { emitter ->
        currentFirebaseUser()!!.delete().addOnCompleteListener { task ->
            if (!emitter.isDisposed) {
                if (task.isSuccessful) emitter.onComplete()
                else emitter.onError(task.exception!!)
            }
        }
    }

    fun isFavBook(book: Book) = Completable.create { emitter ->
        db.collection(FAV_BOOKS_COLLECTION)
            .document(currentFirebaseUser()!!.uid)
            .collection(FAV_BOOKS_SUBCOLLECTION)
            .document(book.title.toString())
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) emitter.onComplete()
            }
    }

    fun saveFavBook(likedBook: Book) = Completable.create { emitter ->
        db.collection(FAV_BOOKS_COLLECTION)
            .document(currentFirebaseUser()!!.uid)
            .collection(FAV_BOOKS_SUBCOLLECTION)
            .document(likedBook.title.toString())
            .set(likedBook)
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun deleteFavBook(book: Book) = Completable.create { emitter ->
        db.collection(FAV_BOOKS_COLLECTION)
            .document(currentFirebaseUser()!!.uid)
            .collection(FAV_BOOKS_SUBCOLLECTION)
            .document(book.title.toString())
            .delete()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun getAllFavBooks() = Observable.create<MutableList<Book>> { emitter ->
        val dataList = mutableListOf<Book>()
        db.collection(FAV_BOOKS_COLLECTION)
            .document(currentFirebaseUser()!!.uid)
            .collection(FAV_BOOKS_SUBCOLLECTION)
            .get()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            dataList.add(document.toObject(Book::class.java))
                        }
                        emitter.onNext(dataList)
                    } else {
                        emitter.onError(task.exception!!)
                    }
                }
            }
    }

    fun getAllBooks() = Observable.create<MutableList<Book>> { emitter ->
        val dataList = mutableListOf<Book>()
        db.collection("Books")
            .get()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val book =
                                document.toObject(Book::class.java)
                            val chapterList = ArrayList<Chapter>()
                            db.collection("Books").document(document.id).collection("Chapters")
                                .get()
                                .addOnSuccessListener { chapters ->
                                    for (chapter in chapters) {
                                        chapterList.add(chapter.toObject(Chapter::class.java))
                                    }
                                }
                            book.chapters = chapterList
                            dataList.add(book)
                        }
                        emitter.onNext(dataList)
                    } else {
                        emitter.onError(Exception("Books not found..."))
                    }
                }
            }
    }

    fun savePremiumAccount() = Completable.create { emitter ->
        db.collection(PREMIUM_ACCOUNTS_COLLECTION)
            .document(currentFirebaseUser()!!.uid)
            .set(mapOf(PREMIUM_ACCOUNT_KEY to currentFirebaseUser()!!.uid))
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun deletePremiumAccount() = Completable.create { emitter ->
        db.collection(PREMIUM_ACCOUNTS_COLLECTION)
            .document(currentFirebaseUser()!!.uid)
            .delete()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun isPremiumAccount() = Completable.create { emitter ->
        db.collection(PREMIUM_ACCOUNTS_COLLECTION)
            .document(currentFirebaseUser()!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful && task.result != null && task.result!!.exists()) emitter.onComplete()
                    else emitter.onError(PremiumAccountNotFoundException("Premium account not found!"))
                }
            }
    }
}