package com.blazebooks.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import coil.api.clear
import coil.api.load
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.login.LoginActivity
import com.blazebooks.ui.preferences.SettingsActivity
import com.blazebooks.ui.search.SearchActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_set_profile_img.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : PreconfiguredActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    //img url
    private val owlImg = "https://cdn.pixabay.com/photo/2013/07/13/11/34/owl-158411_960_720.png"
    private val goatImg = "https://cdn.pixabay.com/photo/2014/04/03/00/35/goat-308775_960_720.png"
    private val monkeyImg =
        "https://cdn.pixabay.com/photo/2015/01/22/12/58/monkey-607708_960_720.png"
    private val catImg = "https://cdn.pixabay.com/photo/2013/07/12/14/32/cat-148436_960_720.png"
    private val tuxImg = "https://cdn.pixabay.com/photo/2013/07/13/13/42/tux-161406_960_720.png"
    private val foxImg = "https://cdn.pixabay.com/photo/2016/07/15/08/02/fox-1518438_960_720.png"

    /**
     * @param savedInstanceState
     * @author Victor Gonzalez
     * @author Mounir Zbayr
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val header = navView.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.nav_header_tv_userName)
        val email = header.findViewById<TextView>(R.id.nav_header_tv_userEmail)

        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        fab.setOnClickListener { view ->
            Snackbar.make(
                view,
                "Continue Reading Button. Not Implemented yet =(",
                Snackbar.LENGTH_LONG
            )
                .setAction("Action", null).show()
        }

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

        //set username and email view
        name.text = auth.currentUser?.displayName.toString()
        email.text = auth.currentUser?.email.toString()
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu
     * @return Boolean
     * @author Victor Gonzalez
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Switch actions depending on the menu item touched.
     *
     * @return Boolean
     * @param item
     * @author Victor Gonzalez
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> signOut()
            R.id.action_settings -> goToPreferenceActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Returns to previous activity and sets custom animation transition.
     *
     * @author Victor Gonzalez
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation, R.anim.zoom_out)
        finish()
    }

    /**
     * Starts a new activity and pass a String by param
     * depending of the view.id
     *
     * @param view
     * @author Victor Gonzalez
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
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Creates a new dialog for choose the profile image.
     *
     * @author Victor Gonzalez
     */
    @SuppressLint("InflateParams")
    fun setProfileImage(view: View) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_set_profile_img, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val mAlertDialog = mBuilder.show()

        mDialogView.apply {
            dialogImg01.apply {
                load(owlImg)
                setOnClickListener {
                    //TODO -> save it!
                    nav_view.imageView.clear()
                    nav_view.imageView.load(owlImg)
                    mAlertDialog.dismiss()
                }
            }

            dialogImg02.apply {
                load(goatImg)
                setOnClickListener {
                    //TODO -> save it!
                    nav_view.imageView.clear()
                    nav_view.imageView.load(goatImg)
                    mAlertDialog.dismiss()
                }
            }

            dialogImg03.apply {
                load(monkeyImg)
                setOnClickListener {
                    //TODO -> save it!
                    nav_view.imageView.clear()
                    nav_view.imageView.load(monkeyImg)
                    mAlertDialog.dismiss()
                }
            }

            dialogImg11.apply {
                load(catImg)
                setOnClickListener {
                    //TODO -> save it!
                    nav_view.imageView.clear()
                    nav_view.imageView.load(catImg)
                    mAlertDialog.dismiss()
                }
            }

            dialogImg12.apply {
                load(tuxImg)
                setOnClickListener {
                    //TODO -> save it!
                    nav_view.imageView.clear()
                    nav_view.imageView.load(tuxImg)
                    mAlertDialog.dismiss()
                }
            }

            dialogImg13.apply {
                load(foxImg)
                setOnClickListener {
                    //TODO -> save it!
                    nav_view.imageView.clear()
                    nav_view.imageView.load(foxImg)
                    mAlertDialog.dismiss()
                }
            }
        }
    }

    /**
     *  Signs out from the current session.
     *
     * @see LoginActivity
     * @author Mounir
     */
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.static_animation, R.anim.zoom_out)
        finish()
    }

    /**
     * Goes to SettingsActivity
     *
     * @see SettingsActivity
     * @author Victor Gonzalez
     */
    private fun goToPreferenceActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

}
