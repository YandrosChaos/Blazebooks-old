package com.blazebooks.ui.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import coil.api.load
import com.blazebooks.R
import com.blazebooks.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.activity_show_book_item.*

class ShowBookActivity : AppCompatActivity() {
    private val adapter by lazy { ViewPagerAdapter(this) }
    private var liked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_book)
        activityShowBookViewPager.adapter = adapter

        //crear las diferentes pestañas
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
        //default option
    }

    fun addFav(view: View) {
        liked = when (liked) {
            true -> {
                //TODO -> remove from favs
                //showBookBtnFav.load(R.drawable.ic_like_remove)
                showBookBtnFav.background = ContextCompat.getDrawable(this,R.drawable.ic_like_remove)
                false
            }
            false -> {
                //TODO -> add to favs
                //showBookBtnFav.load(R.drawable.ic_like_add)
                showBookBtnFav.background = ContextCompat.getDrawable(this,R.drawable.ic_like_add)
                true
            }
        }
        view.refreshDrawableState()
    }
}
