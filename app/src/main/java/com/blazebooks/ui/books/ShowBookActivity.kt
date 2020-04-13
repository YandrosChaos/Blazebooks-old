package com.blazebooks.ui.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blazebooks.R
import com.blazebooks.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_show_book.*

class ShowBookActivity : AppCompatActivity() {
    private val adapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_book)
        activityShowBookViewPager.adapter = adapter

        val tabLayoutMediator =
            TabLayoutMediator(activityShowBookTabLayout, activityShowBookViewPager,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = getString(R.string.synopsis)
                        }
                        1 -> {
                            tab.text = getString(R.string.chapters)
                        }
                    }
                })
        tabLayoutMediator.attach()
    }
}
