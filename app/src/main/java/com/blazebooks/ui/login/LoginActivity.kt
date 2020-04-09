package com.blazebooks.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blazebooks.R
import com.blazebooks.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginClicked(view: View) {
        if (userExist()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Snackbar.make(
                view,
                "Username or password incorrect!",
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    private fun userExist(): Boolean {
        return loginActivityUserName.text.toString() == "whoami" && loginActivityUserPasswd.text.toString() == "root"
    }

    fun throwSingInActivity(view: View) {
        startActivity(Intent(this, SingInActivity::class.java))
        finish()
    }
}
