package com.blazebooks.ui.customdialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import coil.api.load
import com.blazebooks.R
import java.lang.ClassCastException

/**
 * Custom dialog. Lets choose a profile image and returns it by
 * the listener.
 *
 * @author Victor Gonzalez
 */
class ProfileImageDialog : DialogFragment() {
    private lateinit var listener: ProfileImageDialogListener

    interface ProfileImageDialogListener {
        fun onReturnImageSelected(dialog: ProfileImageDialog)
        fun onExitProfileImageDialog(dialog: ProfileImageDialog)
    }

    var selectedImage: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_set_profile_img, container, false)
    }

    /**
     *  Added images to the view dynamically and sets the click listeners. Takes the
     *  URLs from arrays.xml.
     *
     * @see createImageView
     * @author Victor Gonzalez
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogSetImgMainLl = view.findViewById<LinearLayout>(R.id.dialogSetImgMainLl)
        val dialogSetImgCloseBtn = view.findViewById<ImageButton>(R.id.dialogSetImgCloseBtn)

        //set images
        resources.getStringArray(R.array.profile_img_url).forEach {
            dialogSetImgMainLl.addView(createImageView(it))
        }

        //exit click listener
        dialogSetImgCloseBtn.setOnClickListener {
            listener.onExitProfileImageDialog(this)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ProfileImageDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ProfileImageDialogListener")
        }
    }

    /**
     * Receives an url and return an ImageView configured.
     *
     * @param url String that must contain the URL
     * @return ImageView
     *
     * @author Victor Gonzalez
     */
    private fun createImageView(url: String): ImageView {
        return ImageView(context).also { imageView ->
            imageView.layoutParams = ViewGroup.LayoutParams(300, 300)
            imageView.load(url)
            imageView.setOnClickListener {
                selectedImage = imageView
                listener.onReturnImageSelected(this)
            }
        }
    }
}