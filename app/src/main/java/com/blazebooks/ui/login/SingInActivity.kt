package com.blazebooks.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blazebooks.R
import com.blazebooks.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sing_in.*

class SingInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_in)
    }

    fun singInClicked(view: View) {
        if (userFormat()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Snackbar.make(
                view,
                "Username or password already exists!",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun userExist(): Boolean {
        return singInActivityUserName.text.toString() != "whoami"
    }

    private fun userFormat(): Boolean {
        return userExist() && singInActivityUserPasswd.text.toString() == singInActivityUserPasswdAux.text.toString()
    }

    fun goBack(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
