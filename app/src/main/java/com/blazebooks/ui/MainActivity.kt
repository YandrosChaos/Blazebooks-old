package com.blazebooks.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
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
import com.blazebooks.ui.customdialogs.ProfileImageDialog
import com.blazebooks.ui.login.LoginActivity
import com.blazebooks.ui.settings.SettingsActivity
import com.blazebooks.ui.search.SearchActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_complete.*
import kotlinx.android.synthetic.main.dialog_set_profile_img.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : PreconfiguredActivity(), ProfileImageDialog.ProfileImageDialogListener {

    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    /**
     * @param savedInstanceState
     * @author Victor Gonzalez
     * @author Mounir Zbayr
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_complete)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)

        navView = findViewById(R.id.nav_view)
        val header = navView.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.nav_header_tv_userName)
        val email = header.findViewById<TextView>(R.id.nav_header_tv_userEmail)
        val headerImage = header.findViewById<ImageView>(R.id.nav_header_imageView)

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

        if (auth.currentUser!!.photoUrl != null) {
            headerImage.clear()
            headerImage.load(auth.currentUser!!.photoUrl)
        } else {
            //TODO -> LOAD PROFILE IMAGE FOR NO-GOOGLE ACCOUNTS!
            headerImage.clear()
            headerImage.load(R.drawable.ic_reading_big)
        }

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
     * @see ProfileImageDialog
     *
     * @author Victor Gonzalez
     */
    fun setProfileImage(view: View) {
        mainActivityProfileImgFragment.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
            .replace(R.id.mainActivityProfileImgFragment, ProfileImageDialog())
            .commit()
    }

    /**
     * Receives the ProfileImageDialog results and updates this
     * activity view. Calls other method, closing the dialog.
     *
     * @see ProfileImageDialog
     * @see setProfileImage
     * @see ProfileImageDialog.ProfileImageDialogListener
     * @see onExitProfileImageDialog
     *
     * @author Victor Gonzalez
     */
    override fun onReturnImageSelected(dialog: ProfileImageDialog) {
        if (dialog.selectedImage != null) {
            navView.nav_header_imageView.clear()
            navView.nav_header_imageView.load(dialog.selectedImage!!.drawable)
        }
        onExitProfileImageDialog(dialog)
    }

    /**
     * Closes the dialog.
     *
     * @see setProfileImage
     * @see ProfileImageDialog
     * @see ProfileImageDialog.ProfileImageDialogListener
     *
     * @author Victor Gonzalez
     */
    override fun onExitProfileImageDialog(dialog: ProfileImageDialog) {
        mainActivityProfileImgFragment.visibility = View.GONE
        dialog.dismiss()
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
