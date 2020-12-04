package com.blazebooks.ui.reader

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blazebooks.R
import com.blazebooks.databinding.ActivityReaderBinding

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: ArrayList<Fragment>,
    private val context: Context,
    private val binding: ActivityReaderBinding
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    /**
     * Muestra el número de pagina actual
     */
    private fun updatePageTextView(position: Int, itemCount: Int) {
        binding.tNumPages.text =
            String.format(
                context.resources.getString(R.string.pageNumber),
                position,
                itemCount
            )
    }
}