package com.blazebooks.ui.main.about

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.blazebooks.R
import com.blazebooks.util.positiveAlertDialog
import kotlinx.android.synthetic.main.dialog_social_network.view.*

class AboutFragmentAdapter(
    private var itemList: List<Pair<String, Int>>,
    private val activity: Context
) :
    RecyclerView.Adapter<AboutViewHolder>() {

    /**
     * Inflates the layout.
     *
     * @see AboutViewHolder
     *
     * @param parent
     * @param viewType
     *
     * @return CustomViewHolder
     *
     * @author Victor Gonzalez
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fragment_about_card, parent, false)
        return AboutViewHolder(view)
    }

    /**
     * Returns the list size
     *
     * @return Int
     * @author Victor Gonzalez
     */
    override fun getItemCount(): Int {
        return itemList.size
    }

    /**
     * <p>Sets the view and the click listener with the item data. Sets a different view for
     * a PREMIUM or a FREE book. </p>
     * <p>The click listener starts a new dialog when is clicked. This new dialog shows
     * more information about the book.</p>
     *
     * @param holder
     * @param position
     *
     *
     * @author Victor Gonzalez
     */
    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        val dialog: AlertDialog
        val item = itemList[position]
        holder.cardTitle.text = item.first
        holder.imageViewSrc.load(item.second)

        if (item.first == activity.getString(R.string.social_networks)) {
            dialog = createSocialNetworkDialog()
        } else {
            dialog = activity.positiveAlertDialog(
                item.first,
                when (item.first) {
                    activity.getString(R.string.about) -> {
                        "Blazebooks nace dentro de la mente de dos programadores, como trabajo final de grado.\n" +
                                "\nEs una aplicación que interconecta lo nuevo y lo viejo; una nueva forma de vivir la literatura y la cultura."
                    }
                    activity.resources.getString(R.string.become_author) -> {
                        "Not implemented... by de moment >:v"
                    }
                    activity.getString(R.string.credits_title) -> {
                        activity.getString(R.string.credits)
                    }
                    else -> "Not implemented yet."
                },
                "Close"
            )
        }

        //set click listener
        holder.layout.setOnClickListener {
            dialog.show()
        }
    }

    /**
     * Creates the SocialNetwork dialog and return it.
     */
    private fun createSocialNetworkDialog(): AlertDialog {
        val builder =
            AlertDialog.Builder(ContextThemeWrapper(activity, R.style.AlertDialogTheme))
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