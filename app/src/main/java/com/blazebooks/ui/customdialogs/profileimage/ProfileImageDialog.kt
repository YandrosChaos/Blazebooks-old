package com.blazebooks.ui.customdialogs.profileimage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.api.load
import com.blazebooks.R
import com.blazebooks.databinding.DialogSetProfileImgBinding
import com.blazebooks.util.snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * Custom dialog. Lets choose a profile image and returns it by
 * the listener.
 *
 * @author Victor Gonzalez
 */
class ProfileImageDialog : DialogFragment(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<ProfileImageViewModelFactory>()

    private lateinit var binding: DialogSetProfileImgBinding
    private lateinit var viewModel: ProfileImageDialogViewModel
    private lateinit var listener: ProfileImageDialogListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_set_profile_img, container, false)
        viewModel = ViewModelProvider(this, factory).get(ProfileImageDialogViewModel::class.java)
        return binding.root
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
        val dialogSetImgCleanBtn = view.findViewById<ImageButton>(R.id.dialogSetImgCleanBtn)

        //set images
        resources.getStringArray(R.array.profile_img_url).forEach {
            dialogSetImgMainLl.addView(createImageView(it))
        }

        //exit click listener
        dialogSetImgCloseBtn.setOnClickListener {
            listener.onExitProfileImageDialog(this)
        }

        dialogSetImgCleanBtn.setOnClickListener {
            updateProfileImage(null)
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
     * Receives an url and return an configured ImageView.
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
                updateProfileImage(url)
            }
        }
    }

    /**
     * Se le proporciona una url de imagen con la que actualiza el perfil.
     * Si se elimina la imagen, utiliza una por defecto.
     *
     * @param url
     *
     * @author Victor Gonzalez
     */
    private fun updateProfileImage(url: String?) {
        lifecycleScope.launch {
            try {
                viewModel.updatePhotoUri(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        binding.root.snackbar("Success!")
                        listener.onExitProfileImageDialog(this@ProfileImageDialog)

                    }, {
                        binding.root.snackbar("An error occurred.!")
                    })
            } catch (e: Exception) {
                binding.root.snackbar("Check your internet connection please.")
            }
        }
    }
}