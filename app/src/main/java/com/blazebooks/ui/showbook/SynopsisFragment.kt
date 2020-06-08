package com.blazebooks.ui.showbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import coil.transform.BlurTransformation
import com.blazebooks.util.Constants
import com.blazebooks.R
import kotlinx.android.synthetic.main.item_show_book.*
import kotlinx.android.synthetic.main.fragment_show_book_synopsis.*

class SynopsisFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_book_synopsis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set the images
        if (Constants.CURRENT_BOOK.image != null) {
            showBookItemBackgroundImg.load(Constants.CURRENT_BOOK.image) {
                crossfade(true)
                transformations(
                    BlurTransformation(requireContext().applicationContext!!)
                )
            }
            showBookItemImg.load(Constants.CURRENT_BOOK.image) {
                crossfade(true)
            }
        }

        //set premium
        if (!Constants.CURRENT_BOOK.premium) showBookItemPremiumImg.visibility = View.GONE

        //Set Texts
        showBookTvTitle.text = Constants.CURRENT_BOOK.title
        showBookTvAuthorAux.text = Constants.CURRENT_BOOK.author
        showBookTvChaptersAux.text = Constants.CURRENT_BOOK.chapters.size.toString()
        showBookTvGenreAux.text = Constants.CURRENT_BOOK.genre

        //set synopsis
        fragmentShowBookSynopsis.text = Constants.CURRENT_BOOK.synopsis

    }
}