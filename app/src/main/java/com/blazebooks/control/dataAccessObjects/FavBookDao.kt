package com.blazebooks.control.dataAccessObjects

import com.blazebooks.model.FavBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FavBookDao {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getAll(): List<FavBook> {
        val listOfFavBooks = arrayListOf<FavBook>()
        val currentFirebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (currentFirebaseUser != null) {
            db.collection("FavBooks").whereEqualTo("user_id", currentFirebaseUser.uid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            listOfFavBooks.add(document.toObject(FavBook::class.java))
                        }
                    } else {
                        throw  DaoException("Error getting docs!", task.exception)
                    }
                }
        }

        return listOfFavBooks
    }

    fun exist(favBook: FavBook): Boolean {
        var favBookResult = FavBook()

        db.collection("FavBooks")
            .document(favBook.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doc = task.result
                    if(doc!= null){
                        favBookResult = doc.toObject(FavBook::class.java)!!
                    }else{
                        throw DaoException("The document is null!")
                    }
                }else{
                    throw  DaoException("Error getting the doc!", task.exception)
                }

            }
        return !favBookResult.isEmpty()
    }

    fun insert(favBook: FavBook) {
        val currentFirebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        favBook.user_id = currentFirebaseUser!!.uid
        db.collection("FavBooks").document(favBook.toString()).set(favBook)
    }

    fun delete(favBook: FavBook) {
        db.collection("FavBooks").document(favBook.toString()).delete()
    }
}