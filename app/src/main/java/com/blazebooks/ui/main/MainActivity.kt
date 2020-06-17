package com.blazebooks.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.ui.customdialogs.profileimage.ProfileImageDialog
import com.blazebooks.ui.auth.LoginActivity
import com.blazebooks.ui.customdialogs.profileimage.ProfileImageDialogListener
import com.blazebooks.ui.settings.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_complete.*

class MainActivity : PreconfiguredActivity(), ProfileImageDialogListener, MainViewModelListener {

    private lateinit var navView: NavigationView
    private lateinit var headerImage: ImageView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    /**
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

        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()


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
        //setUpProfileDataView()
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
     * @see onSetProfileImageDialog
     * @see onExitProfileImageDialog
     *
     * @author Victor Gonzalez
     */
    override fun onReturnImageSelected(dialog: ProfileImageDialog) {
        //setUpProfileDataView()
        onExitProfileImageDialog(dialog)
    }

    /**
     * Restores the default profile image.
     *
     * @see onExitProfileImageDialog
     * @see ProfileImageDialog
     * @see onSetProfileImageDialog
     *
     * @author Victor Gonzalez
     */
    override fun onCleanProfileImage(dialog: ProfileImageDialog) {
        //setUpProfileDataView()
        onExitProfileImageDialog(dialog)
    }

    /**
     * Closes the dialog.
     *
     * @see onSetProfileImageDialog
     * @see ProfileImageDialog
     *
     * @author Victor Gonzalez
     */
    override fun onExitProfileImageDialog(dialog: ProfileImageDialog) {
        mainActivityProfileImgFragment.visibility = View.GONE
        dialog.dismiss()
    }

    /**
     * Creates a new dialog for choose the profile image.
     *
     * @see ProfileImageDialog
     *
     * @author Victor Gonzalez
     */
    fun onSetProfileImageDialog(view: View) {
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
     * Sets the profile image.
     *
     * @author Victor Gonzalez
     */
    override fun onLoadImage(image: String?, defaultImage: Int) {
        headerImage.clear()
        if (!image.isNullOrEmpty()) {
            headerImage.load(image)
        } else {
            headerImage.load(defaultImage)
        }
    }

    /*
    /**
     * Sets email, username and profile image.
     *
     * @author Victor Gonzalez
     */
    private fun setUpProfileDataView() {
        //set username and email view
        name.text = auth.currentUser?.displayName.toString()
        email.text = auth.currentUser?.email.toString()

        if (!sharedPreferences.getString(SELECTED_PROFILE_IMAGE_KEY, null)
                .isNullOrEmpty()
        ) {
            //local image stored
            headerImage.clear()
            headerImage.load(
                sharedPreferences.getString(
                    SELECTED_PROFILE_IMAGE_KEY,
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

     */
}
