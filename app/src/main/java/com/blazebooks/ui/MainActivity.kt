package com.blazebooks.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.login.LoginActivity
import com.blazebooks.ui.search.SearchActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(
                view,
                "Continue Reading Button. Not Implemented yet =(",
                Snackbar.LENGTH_LONG
            )
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        tv_userName.text = auth.currentUser?.displayName
        tv_userEmail.text = auth.currentUser?.email
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Starts a new activity and pass a String by param
     * depending of the view.id
     *
     * @param view
     */
    fun searchBooks(view: View) {
        startActivity(
            Intent(this, SearchActivity::class.java).apply {
                putExtra(
                    Constants.TOOLBAR_TITLE_CODE,
                    when (view.id) {
                        R.id.fragmentBooksBook ->
                            getString(R.string.books)
                        R.id.fragmentBooksIb ->
                            getString(R.string.interactive_books)
                        R.id.fragmentBooksAll ->
                            getString(R.string.all_books)
                        R.id.fragmentHomeMyBooks ->
                            getString(R.string.my_books)
                        else -> "SEARCH"
                    }

                )
            }
        )
    }

    /**
     *  Signs out from the current session.
     *
     * @param item
     */
    fun signOut(item: MenuItem) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}
