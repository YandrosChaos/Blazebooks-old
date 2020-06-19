package com.blazebooks.ui.main.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.blazebooks.R
import kotlinx.android.synthetic.main.fragment_books.view.*
import com.blazebooks.databinding.FragmentBooksBinding

private const val CORNER_RADIUS = 60f

/**
 * Catalog fragment. Shows the images
 *
 * @author  Victor Gonzalez
 */
class CatalogFragment : Fragment() {
    private lateinit var binding: FragmentBooksBinding
    private lateinit var viewModel: CatalogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_books, container, false)
        viewModel = ViewModelProvider(this).get(CatalogViewModel::class.java)
        binding.viewModel = viewModel
        loadImages()
        return binding.root
    }

    private fun loadImages() {
        binding.root.fragmentBooksImgAll.apply {
            load(R.drawable.old_books) {
                crossfade(true)
                transformations(
                    BlurTransformation(requireContext().applicationContext!!),
                    RoundedCornersTransformation(CORNER_RADIUS)
                )
            }
        }

        binding.root.fragmentBooksImgBook.load(R.drawable.lamp_books) {
            crossfade(true)
            transformations(
                BlurTransformation(requireContext().applicationContext!!),
                RoundedCornersTransformation(CORNER_RADIUS)
            )
        }

        binding.root.fragmentBooksImgIb.load(R.drawable.skull_books) {
            crossfade(true)
            transformations(
                BlurTransformation(requireContext().applicationContext!!),
                RoundedCornersTransformation(CORNER_RADIUS)
            )
        }
    }
}
