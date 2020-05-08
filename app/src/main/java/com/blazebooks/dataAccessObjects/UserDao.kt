package com.blazebooks.dataAccessObjects

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import com.blazebooks.model.User
import com.blazebooks.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest.Builder
import com.google.firebase.firestore.FirebaseFirestore


class UserDao(
    //val view: View,
    val context: Context
) : DAO<User> {

    //Necesario para la autenticación
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseUser = auth.currentUser


    override fun getAll(): List<User> {
        TODO("Not yet implemented")
    }

    override fun get(title: String): User {
        TODO("Not yet implemented")
    }

    override fun insert(user: User) {

        // Crea el usuario en la base de Firebase y si está bien pasa directo al main
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val profileUpdates =
                        Builder()
                            .setDisplayName(user.userName)
                            .setPhotoUri(Uri.parse(user.URLprofile))
                            .build()
                    firebaseUser?.updateProfile(profileUpdates)

                    db.collection("Users").document(firebaseUser?.uid.toString()).set(user)

                    context.startActivity(Intent(context, MainActivity::class.java))
                    if (context is Activity) {
                        context.finish()
                    }
                } else {
/*
                    Snackbar.make(
                        view, task.exception?.message.toString()
                        ,
                        Snackbar.LENGTH_LONG
                    ).show()
 */
                }//if


            }
    }

    override fun delete(user: User) {

        firebaseUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }

    }//delete

    override fun update(user: User) {
        val profileUpdates = Builder()
            .setDisplayName("Nuevo Nombre")
            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
            .build()

        firebaseUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }//update


}//class



