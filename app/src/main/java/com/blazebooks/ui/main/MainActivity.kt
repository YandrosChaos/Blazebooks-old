package com.blazebooks.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import coil.api.clear
import coil.api.load
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.databinding.ActivityMainCompleteBinding
import com.blazebooks.ui.customdialogs.profileimage.ProfileImageDialog
import com.blazebooks.ui.customdialogs.profileimage.ProfileImageDialogListener
import com.blazebooks.ui.reader.ReaderActivity
import com.blazebooks.ui.settings.SettingsActivity
import com.blazebooks.util.PATH_CODE
import com.blazebooks.util.snackbar
import com.blazebooks.util.startLoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main_complete.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : PreconfiguredActivity(), ProfileImageDialogListener, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<MainViewModelFactory>()
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainCompleteBinding

    private lateinit var navView: NavigationView
    private lateinit var headerImage: ImageView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     * @param savedInstanceState
     *
     * @author Victor Gonzalez
     * @author Mounir Zbayr
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_complete)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val playButton: FloatingActionButton = findViewById(R.id.playButton)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)

        navView = findViewById(R.id.nav_view)
        val header = navView.getHeaderView(0)

        name = header.findViewById(R.id.nav_header_tv_userName)
        email = header.findViewById(R.id.nav_header_tv_userEmail)
        headerImage = header.findViewById(R.id.nav_header_imageView)

        setSupportActionBar(toolbar)

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
        onSetupGUI()

        playButton.setOnClickListener {
            onLastBookClicked()
        }

        headerImage.setOnClickListener {
            onSetProfileImageDialog()
        }
    }

    /**
     * If exist a last book stored into shared preferences, then shows it. Else,
     * shows a snackbar.
     *
     * @author Victor Gonzalez
     */
    private fun onLastBookClicked() {
        if (!viewModel.urlBook.isNullOrEmpty()) {
            Intent(this, ReaderActivity::class.java).also {
                it.putExtra(PATH_CODE, viewModel.urlBook)
                startActivity(it)
            }
        } else {
            binding.root.snackbar("Last book cannot be found.")
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
            R.id.action_sign_out -> {
                viewModel.logout()
                startLoginActivity()
            }
            R.id.action_settings -> onSettingsActivity()
        }
        return super.onOptionsItemSelected(item)
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
        onSetupGUI()
        dialog.dismiss()
    }

    /**
     * Creates a new dialog for choose the profile image.
     *
     * @see ProfileImageDialog
     *
     * @author Victor Gonzalez
     */
    private fun onSetProfileImageDialog() {
        mainActivityProfileImgFragment.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
            .replace(R.id.mainActivityProfileImgFragment, ProfileImageDialog())
            .commit()
    }

    /**
     * Sets the profile image.
     *
     * @author Victor Gonzalez
     */
    private fun onSetupGUI() {
        name.text = viewModel.username
        email.text = viewModel.usermail
        headerImage.clear()
        headerImage.load(viewModel.getStoredProfileImage())
    }

    /**
     * Goes to SettingsActivity
     *
     * @see SettingsActivity
     * @author Victor Gonzalez
     */
    private fun onSettingsActivity() {
        Intent(this, SettingsActivity::class.java).also {
            startActivity(it)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }
}
