package com.blazebooks.data.dataAccessObjects

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.blazebooks.model.User
import com.blazebooks.util.DaoException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest.Builder
import com.google.firebase.firestore.FirebaseFirestore


class UserDao() : DAO<User> {

    //Necesario para la autenticación
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun getAll(): List<User> {
        val listOfUsers = arrayListOf<User>()

        db.collection("Users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        //TODO -> transformar el documento a user
                        listOfUsers.add(document.toObject(User::class.java))
                    }
                } else {
                    throw  DaoException("Error getting docs!", task.exception)
                }
            }

        return listOfUsers
    }

    override fun get(id: String): User {
        var user: User = User()

        db.collection("Users")
            .document(id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        user = document.toObject(User::class.java)!!
                    } else {
                        throw DaoException("The document is null!", task.exception)
                    }
                } else {
                    throw  DaoException("Error getting the doc!", task.exception)
                }
            }

        return user
    }

    override fun insert(user: User) {
        // Crea el usuario en la base de Firebase
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val firebaseUser = auth.currentUser

                    val profileUpdates =
                        Builder()
                            .setDisplayName(user.userName)
                            .setPhotoUri(Uri.parse(user.URLprofile))
                            .build()

                    firebaseUser?.updateProfile(profileUpdates)

                    db.collection("Users").document(firebaseUser?.uid.toString()).set(user.toMap())


                } else {
                    throw  DaoException("Error", task.exception)
                }//if
            }
    }//insert

    override fun delete(id: String) {

        val firebaseUser = auth.currentUser
        firebaseUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                } else throw  DaoException("Error", task.exception)
            }

        db.collection("Users").document(id).delete()

    }//delete

    override fun update(user: User) {

        val profileUpdates = Builder()
            .setDisplayName(user.userName)
            .setPhotoUri(Uri.parse(user.URLprofile))
            .build()

        val firebaseUser = auth.currentUser
        firebaseUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }

        firebaseUser?.updateEmail(user.email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User email updated.")
            }
        }

        firebaseUser?.updatePassword(user.password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User password updated.")
            }
        }

        db.collection("Users").document(firebaseUser?.uid.toString()).set(user.toMap())
    }//update

}//class



