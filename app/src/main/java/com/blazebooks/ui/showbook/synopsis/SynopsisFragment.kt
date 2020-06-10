package com.blazebooks.ui.showbook.synopsis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import coil.transform.BlurTransformation
import com.blazebooks.R
import com.blazebooks.util.CURRENT_BOOK
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
        if (CURRENT_BOOK.image != null) {
            showBookItemBackgroundImg.load(CURRENT_BOOK.image) {
                crossfade(true)
                transformations(
                    BlurTransformation(requireContext().applicationContext!!)
                )
            }
            showBookItemImg.load(CURRENT_BOOK.image) {
                crossfade(true)
            }
        }

        //set premium
        if (!CURRENT_BOOK.premium) showBookItemPremiumImg.visibility = View.GONE

        //Set Texts
        showBookTvTitle.text = CURRENT_BOOK.title
        showBookTvAuthorAux.text = CURRENT_BOOK.author
        showBookTvChaptersAux.text = CURRENT_BOOK.chapters.size.toString()
        showBookTvGenreAux.text = CURRENT_BOOK.genre

        //set synopsis
        fragmentShowBookSynopsis.text = CURRENT_BOOK.synopsis

    }
}