package com.blazebooks.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blazebooks.R
import com.blazebooks.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_forgot_passwd.view.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth //Necesario para la autenticación

    /**
     * @param savedInstanceState
     * @author Mounir Zbayr
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
    }

    /**
     *
     * @author Mounir Zbayr
     */
    fun loginClicked(view: View) {

        if (loginActivityUserName.text.toString().isEmpty()) {
            loginActivityUserName.error = "Please enter email"
            loginActivityUserName.requestFocus()
            return
        }

        if (loginActivityUserPasswd.text.toString().isEmpty()) {
            loginActivityUserPasswd.error = "Password cannot be empty"
            loginActivityUserPasswd.requestFocus()
            return
        }


        //Comprueba que los datos coinciden y si es así actualiza el updateUI con el usuario , si no lo pone a nulo
        auth.signInWithEmailAndPassword(
            loginActivityUserName.text.toString(),
            loginActivityUserPasswd.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        baseContext, "Username or password incorrect!.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }//loginClicked

    /**
     * Si el usuario no es nulo se pasa al main
     *
     * @author Mounir Zbayr
     */
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    /**
     * Comprueba al iniciar si el usuario es nulo. Si lo es se muestra
     * la vista del loguin y si no pasa directo al main
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    public override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }//onStart

    /**
     * Sends an email for reset the password.
     *
     * @param view
     * @author Mounir Zbayr
     */
    fun sendPasswordResetEmail(view: View) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_passwd, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()
        //set button onClickListener
        mDialogView.forgotPasswdBtn.setOnClickListener {
            if (mDialogView.forgotpwdDialogUserName.text.isNotEmpty()) {
                auth.sendPasswordResetEmail(mDialogView.forgotpwdDialogUserName.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            mAlertDialog.dismiss()
                            Toast.makeText(this, "Email sent =)", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }else{
                Toast.makeText(this,"Please, introduce your email account.", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Throws SignInActivity and finish this activity
     *
     * @param view
     * @author Victor Gonzalez
     */
    fun throwSignInActivity(view: View) {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}
