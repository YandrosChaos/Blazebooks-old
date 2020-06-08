package com.blazebooks.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
import androidx.preference.PreferenceManager
import coil.api.clear
import coil.api.load
import com.blazebooks.util.Constants
import com.blazebooks.R
import com.blazebooks.model.PreconfiguredActivity
import com.blazebooks.ui.dialogs.ProfileImageDialog
import com.blazebooks.ui.login.LoginActivity
import com.blazebooks.ui.reader.ReaderActivity
import com.blazebooks.ui.settings.SettingsActivity
import com.blazebooks.ui.search.SearchActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_complete.*

class MainActivity : PreconfiguredActivity(), ProfileImageDialog.ProfileImageDialogListener {

    private lateinit var navView: NavigationView
    private lateinit var headerImage: ImageView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    /**
     * @see setUpProfileDataView
     *
     * @param savedInstanceState
     *
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
        name = header.findViewById(R.id.nav_header_tv_userName)
        email = header.findViewById(R.id.nav_header_tv_userEmail)
        headerImage = header.findViewById(R.id.nav_header_imageView)

        sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)

        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()

        /**
         * If exist a last book stored into shared preferences, then shows it. Else,
         * shows a snackbar.
         *
         * @author Victor Gonzalez
         */
        fab.setOnClickListener { view ->
            val lastBookUrl = sharedPreferences.getString(Constants.LAST_BOOK_SELECTED_KEY, null)

            if (!lastBookUrl.isNullOrEmpty()) {
                startActivity(
                    Intent(this, ReaderActivity::class.java).apply
                    { putExtra(Constants.PATH_CODE, lastBookUrl) }
                )
                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
            } else {
                Snackbar.make(
                    view,
                    "Last book cannot be found.",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null).show()
            }
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

        //set username, email and profile image
        setUpProfileDataView()
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
     * Receives the ProfileImageDialog results and updates this
     * activity view. Calls other method, closing the dialog.
     *
     * @see ProfileImageDialog
     * @see setProfileImage
     * @see ProfileImageDialog.ProfileImageDialogListener
     * @see onExitProfileImageDialog
     * @see setUpProfileDataView
     *
     * @author Victor Gonzalez
     */
    override fun onReturnImageSelected(dialog: ProfileImageDialog) {
        setUpProfileDataView()
        onExitProfileImageDialog(dialog)
    }

    /**
     * Restores the default profile image.
     *
     * @see setUpProfileDataView
     * @see onExitProfileImageDialog
     * @see ProfileImageDialog
     * @see ProfileImageDialog.ProfileImageDialogListener
     * @see setProfileImage
     *
     * @author Victor Gonzalez
     */
    override fun onCleanProfileImage(dialog: ProfileImageDialog) {
        setUpProfileDataView()
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
                            getString(R.string.fav_books)
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
     *  Signs out from the current session and clean the SharedPreferences.
     *
     * @see LoginActivity
     *
     * @author Mounir
     * @author Victor Gonzalez
     */
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        sharedPreferences.edit().clear().apply()
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

    /**
     * Sets email, username and profile image.
     *
     * @author Victor Gonzalez
     */
    private fun setUpProfileDataView() {
        //set username and email view
        name.text = auth.currentUser?.displayName.toString()
        email.text = auth.currentUser?.email.toString()

        if (!sharedPreferences.getString(Constants.SELECTED_PROFILE_IMAGE_KEY, null)
                .isNullOrEmpty()
        ) {
            //local image stored
            headerImage.clear()
            headerImage.load(
                sharedPreferences.getString(
                    Constants.SELECTED_PROFILE_IMAGE_KEY,
                    null
                )
            )
        } else if (auth.currentUser?.photoUrl != null) {
            //google account image
            headerImage.clear()
            headerImage.load(auth.currentUser!!.photoUrl)
        } else {
            //default image
            headerImage.clear()
            headerImage.load(R.drawable.ic_reading_big)
        }
    }
}
