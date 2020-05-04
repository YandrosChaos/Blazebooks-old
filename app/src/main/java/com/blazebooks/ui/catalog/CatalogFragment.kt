package com.blazebooks.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.blazebooks.R
import kotlinx.android.synthetic.main.fragment_books.view.*

/**
 * Catalog fragment. Shows the images
 *
 * @author  Victor Gonzalez
 */
class CatalogFragment : Fragment() {

    private val cornerRadius: Float = 60f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_books, container, false)

        root.fragmentBooksImgAll.load(R.drawable.old_books) {
            crossfade(true)
            transformations(
                BlurTransformation(requireContext().applicationContext!!),
                RoundedCornersTransformation(cornerRadius)
            )
        }

        root.fragmentBooksImgBook.load(R.drawable.lamp_books) {
            crossfade(true)
            transformations(
                BlurTransformation(requireContext().applicationContext!!),
                RoundedCornersTransformation(cornerRadius)
            )
        }

        root.fragmentBooksImgIb.load(R.drawable.skull_books) {
            crossfade(true)
            transformations(
                BlurTransformation(requireContext().applicationContext!!),
                RoundedCornersTransformation(cornerRadius)
            )
        }
        return root
    }
}
