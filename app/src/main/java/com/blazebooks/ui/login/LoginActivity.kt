package com.blazebooks.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.blazebooks.R
import com.blazebooks.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sing_in.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth; //Necesario para la autenticación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth= FirebaseAuth.getInstance();
    }

    fun loginClicked(view: View) {

        if(loginActivityUserName.text.toString().isEmpty()){
            loginActivityUserName.error = "Please enter email"
            loginActivityUserName.requestFocus()
            return
        }

        if(loginActivityUserPasswd.text.toString().isEmpty()){
            loginActivityUserPasswd.error = "Password cannot be empty"
            loginActivityUserPasswd.requestFocus()
            return
        }


        //Comprueba que los datos coinciden y si es así actualiza el updateUI con el usuario , si no lo pone a nulo
        auth.signInWithEmailAndPassword(loginActivityUserName.text.toString(), loginActivityUserPasswd.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Username or password incorrect!.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        /*if (userExist()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Snackbar.make(
                view,
                "Username or password incorrect!",
                Snackbar.LENGTH_LONG
            ).show()
        }*/

    }

    /**
     * Si el usuario no es nulo se pasa al main
     */
    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Comprueba al iniciar si el usuario es nulo. Si lo es se muestra la vista del loguin y si no pasa directo al main
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun userExist(): Boolean {
        return loginActivityUserName.text.toString() == "whoami" && loginActivityUserPasswd.text.toString() == "root"
    }

    fun throwSingInActivity(view: View) {
        startActivity(Intent(this, SingInActivity::class.java))
        finish()
    }
}
