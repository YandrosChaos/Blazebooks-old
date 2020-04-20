package com.blazebooks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blazebooks.ui.showbook.ChapterFragment
import com.blazebooks.ui.showbook.SynopsisFragment

class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    companion object {
        private const val ARG_OBJECT = "object"
    }

    /**
     *  Indicates the number of tabs in the activity.
     */
    override fun getItemCount(): Int = 2

    /**
     * Inflates the differents fragments.
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                SynopsisFragment()
            }
            else -> {
                val fragment = ChapterFragment()
                /*fragment.arguments = Bundle().apply {

                }*/
                fragment
            }
        }
    }
}