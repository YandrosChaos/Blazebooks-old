package com.blazebooks.ui.becomepremium

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import com.blazebooks.R
import com.blazebooks.model.PreconfiguredActivity
import com.blazebooks.util.scrollToView
import com.blazebooks.util.toast

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
        toast("Not implemented yet =>")
    }

    fun onYearlyVersionClicked(view: View) {
        toast("Not implemented yet =>")
    }
}