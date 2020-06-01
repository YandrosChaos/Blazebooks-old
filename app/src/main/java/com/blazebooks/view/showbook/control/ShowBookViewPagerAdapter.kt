package com.blazebooks.view.showbook.control

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blazebooks.view.showbook.ChapterFragment
import com.blazebooks.view.showbook.SynopsisFragment

/**
 * Inflates the tabs in the host view
 *
 * @author Victor González
 */
class ShowBookViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    /**
     *  Indicates the number of tabs in the activity.
     */
    override fun getItemCount(): Int = 2

    /**
     * Inflates the differents fragments.
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SynopsisFragment()
            else -> ChapterFragment()
        }
    }
}