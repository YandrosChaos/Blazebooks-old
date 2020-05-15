package com.blazebooks.ui.becomepremium

import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import com.blazebooks.R
import com.blazebooks.ui.PreconfiguredActivity

/**
 * @author Victor Gonzalez
 */
class BecomePremiumActivity : PreconfiguredActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var premiumMonthLayout: LinearLayout
    private lateinit var freeLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_become_premium)

        scrollView = findViewById(R.id.becomePremiumMainSV)
        premiumMonthLayout = findViewById(R.id.becomePremiumMainPricesPremiumMonth)
        freeLayout = findViewById(R.id.becomePremiumMainPricesPremiumFree)

    }

    /**
     * Scrolls to Free Plan View.
     */
    fun scrollToFreeVersion(view: View) {
        scrollToView(scrollView, freeLayout)
    }

    /**
     * Scrolls to Monthly Plan View.
     */
    fun scrollToPlans(view: View) {
        scrollToView(scrollView, premiumMonthLayout)
    }

    /**
     * Finish the activity.
     */
    fun onFreeVersionClicked(view: View) {
        finish()
    }

    fun onMonthlyVersionClicked(view: View) {
        Toast.makeText(
            this,
            "Not implemented yet =/",
            Toast.LENGTH_LONG
        ).show()
    }

    fun onYearlyVersionClicked(view: View) {
        Toast.makeText(
            this,
            "Not implemented yet =/",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Used to scroll to the given view.
     *
     * @param scrollViewParent Parent ScrollView
     * @param view View to which we need to scroll.
     *
     * @author from https://stackoverflow.com/questions/21483188/scroll-to-a-specific-view-in-scroll-view
     */
    private fun scrollToView(
        scrollViewParent: ScrollView,
        view: View
    ) {
        // Get deepChild Offset
        val childOffset = Point()
        getDeepChildOffset(scrollViewParent, view.parent, view, childOffset)
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y)
    }

    /**
     * Used to get deep child offset.
     *
     *
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     *
     * @author from https://stackoverflow.com/questions/21483188/scroll-to-a-specific-view-in-scroll-view
     */
    private fun getDeepChildOffset(
        mainParent: ViewGroup,
        parent: ViewParent,
        child: View,
        accumulatedOffset: Point
    ) {
        val parentGroup = parent as ViewGroup
        accumulatedOffset.x += child.left
        accumulatedOffset.y += child.top
        if (parentGroup == mainParent) {
            return
        }
        getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
    }
}
