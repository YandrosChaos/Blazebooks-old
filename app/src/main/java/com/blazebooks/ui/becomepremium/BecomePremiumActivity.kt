package com.blazebooks.ui.becomepremium

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.databinding.ActivityBecomePremiumBinding
import com.blazebooks.util.premium
import com.blazebooks.util.scrollToView
import com.blazebooks.util.snackbar
import com.google.android.gms.common.api.ApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_become_premium.*
import kotlinx.android.synthetic.main.item_free.*
import kotlinx.android.synthetic.main.item_premium_month.*
import kotlinx.android.synthetic.main.item_premium_year.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

/**
 * @author Victor Gonzalez
 */
class BecomePremiumActivity : PreconfiguredActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<BecomePremiumViewModelFactory>()
    private lateinit var viewModel: BecomePremiumViewModel
    private lateinit var binding: ActivityBecomePremiumBinding

    private lateinit var scrollView: ScrollView
    private lateinit var premiumMonthLayout: LinearLayout
    private lateinit var freeLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_become_premium)
        viewModel = ViewModelProvider(this, factory).get(BecomePremiumViewModel::class.java)

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
            binding.root.snackbar("Not implemented yet =>")
        }

        becomePremiumYearlyBtn.setOnClickListener {
            newYearlySubscription()
        }

        //finish the activity
        becomePremiumFreeBtn.setOnClickListener {
            finish()
        }
    }

    private fun newYearlySubscription() {
        lifecycleScope.launch {
            try {
                viewModel.savePremiumUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //success
                        premium = true
                        binding.root.snackbar("Your profile is updated!")
                    }, {
                        //failure
                        binding.root.snackbar("Error!" + it.message)
                    })

            } catch (e: ApiException) {
                binding.root.snackbar("Check your internet connection please.")
            }
        }
    }
}