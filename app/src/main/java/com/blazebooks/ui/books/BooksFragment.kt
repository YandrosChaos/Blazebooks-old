package com.blazebooks.ui.books

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.blazebooks.Constants
import com.blazebooks.R
import kotlinx.android.synthetic.main.fragment_books.view.*

class BooksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_books, container, false)
        root.fragmentBooksAll.load(R.drawable.old_books) {
            crossfade(true)
            transformations(
                BlurTransformation(context!!.applicationContext!!),
                RoundedCornersTransformation(Constants.CORNER_RADIOUS)
            )
        }

        root.fragmentBooksBook.load(R.drawable.lamp_books) {
            crossfade(true)
            transformations(
                BlurTransformation(context!!.applicationContext!!),
                RoundedCornersTransformation(Constants.CORNER_RADIOUS)
            )
        }

        root.fragmentBooksIb.load(R.drawable.skull_books){
            crossfade(true)
            transformations(
                BlurTransformation(context!!.applicationContext!!),
                RoundedCornersTransformation(Constants.CORNER_RADIOUS)
            )
        }
        return root
    }
}
