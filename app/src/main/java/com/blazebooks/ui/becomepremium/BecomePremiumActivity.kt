package com.blazebooks.ui.becomepremium

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.util.scrollToView
import com.blazebooks.util.snackbar
import kotlinx.android.synthetic.main.activity_become_premium.*
import kotlinx.android.synthetic.main.item_free.*
import kotlinx.android.synthetic.main.item_premium_month.*
import kotlinx.android.synthetic.main.item_premium_year.*

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

        //Scrolls to Free Plan View.
        imageButtonClose.setOnClickListener {
            scrollToView(scrollView, freeLayout)
        }

        //Scrolls to Monthly Plan View.
        wannaBePremiumBtn.setOnClickListener {
            scrollToView(scrollView, premiumMonthLayout)
        }

        becomePremiumMonthlyBtn.setOnClickListener {
            it.snackbar("Not implemented yet =>")
        }

        becomePremiumYearlyBtn.setOnClickListener {
            it.snackbar("Not implemented yet =>")
        }

        //finish the activity
        becomePremiumFreeBtn.setOnClickListener {
            finish()
        }

    }
}