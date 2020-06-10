package com.blazebooks.ui.main.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.blazebooks.R
import kotlinx.android.synthetic.main.fragment_books.view.*
import com.blazebooks.databinding.FragmentBooksBinding

/**
 * Catalog fragment. Shows the images
 *
 * @author  Victor Gonzalez
 */
class CatalogFragment : Fragment() {

    private lateinit var viewModel: CatalogViewModel
    private val cornerRadius: Float = 60f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentBooksBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_books, container, false)
        viewModel = ViewModelProviders.of(this).get(CatalogViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.root.fragmentBooksImgAll.load(R.drawable.old_books) {
            crossfade(true)
            transformations(
                BlurTransformation(requireContext().applicationContext!!),
                RoundedCornersTransformation(cornerRadius)
            )
        }

        binding.root.fragmentBooksImgBook.load(R.drawable.lamp_books) {
            crossfade(true)
            transformations(
                BlurTransformation(requireContext().applicationContext!!),
                RoundedCornersTransformation(cornerRadius)
            )
        }

        binding.root.fragmentBooksImgIb.load(R.drawable.skull_books) {
            crossfade(true)
            transformations(
                BlurTransformation(requireContext().applicationContext!!),
                RoundedCornersTransformation(cornerRadius)
            )
        }
        return binding.root
    }
}
