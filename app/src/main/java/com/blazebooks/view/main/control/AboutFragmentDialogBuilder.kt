package com.blazebooks.view.main.control

import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import com.blazebooks.R
import kotlinx.android.synthetic.main.dialog_social_network.view.*

/**
 * Class that creates an AlertDialog custom instance with the received params.
 *
 * @param activity :Context
 * @param item :String with name of custom dialog.
 *
 * @see createAlertDialog
 * @see AboutFragmentAdapter
 * @see AboutViewHolder
 *
 * @author Victor Gonzalez
 */
class AboutFragmentDialogBuilder(
    private val activity: Context,
    private val item: String
) {
    private val builder =
        AlertDialog.Builder(ContextThemeWrapper(activity, R.style.AlertDialogTheme))


    /**
     * Switch between different AlertDialog options. Use the given string. If it not match,
     * return an default error dialog.
     *
     * @return AlertDialog
     */
    fun createAlertDialog(): AlertDialog {

        return when (item) {
            activity.resources.getString(R.string.about) -> createAboutDialog()

            activity.resources.getString(R.string.become_author) -> createBecomeAuthorDialog()

            activity.resources.getString(R.string.social_networks) -> createSocialNetworkDialog()

            activity.resources.getString(R.string.credits_title) -> createCreditsDialog()

            else -> createErrorDialog()
        }
    }


    /**
     * Makes the credits dialog and returns it.
     */
    private fun createCreditsDialog(): AlertDialog {
        return with(builder) {
            setTitle(item)
            setMessage(R.string.credits)
            setPositiveButton("Close", null)
            create()
        }
    }

    /**
     * Makes the BecomeAuthor dialog and return it.
     */
    private fun createBecomeAuthorDialog(): AlertDialog {
        return with(builder) {
            setTitle(item)
            setMessage("Not implemented... by de moment >:v")
            setNegativeButton("Close", null)
            create()
        }
    }

    /**
     * Makes the AboutDialog and return it.
     */
    private fun createAboutDialog(): AlertDialog {
        return with(builder) {
            setTitle(item)
            setMessage("We are blazebooks and bleh bleh bleh... blah blah blah... lorem impsum lorem impsum lorem ipsum ...")
            setPositiveButton("Close", null)
            create()
        }
    }

    /**
     * Makes the error dialog and returns it.
     */
    private fun createErrorDialog(): AlertDialog {
        return with(builder) {
            setTitle("Sorry! :'(")
            setMessage("An error occurred...")
            setPositiveButton("Close", null)
            create()
        }
    }

    /**
     * Creates the SocialNetwork dialog and return it.
     */
    private fun createSocialNetworkDialog(): AlertDialog {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_social_network, null)
        builder.setView(mDialogView)
        return builder.create().apply {
            window!!.setBackgroundDrawableResource(android.R.color.transparent)

            mDialogView.dialogSNCloseBtn.setOnClickListener {
                this.dismiss()
            }

            mDialogView.dialogSNFacebook.setOnClickListener {
                //TODO -> GO TO FACEBOOK
                this.dismiss()
            }
            mDialogView.dialogSNTwitter.setOnClickListener {
                //TODO -> GO TO TWITTER
                this.dismiss()
            }
            mDialogView.dialogSNInstagram.setOnClickListener {
                //TODO -> GO TO INSTAGRAM
                this.dismiss()
            }
        }
    }

}