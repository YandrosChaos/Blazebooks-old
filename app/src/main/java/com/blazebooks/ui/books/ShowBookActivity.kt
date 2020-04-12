package com.blazebooks.ui.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.adapter.ShowBookAdapter
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.app_bar_show_book.*

class ShowBookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_book)
        setUp()
        activityShowBookChapters.layoutManager = LinearLayoutManager(this)
        activityShowBookChapters.adapter =
            ShowBookAdapter(
                Constants.CURRENT_BOOK.chapters,
                this
            )
    }

    /**
     * Restores toolbar to default value.
     */
    override fun onDestroy() {
        super.onDestroy()
        DrawableCompat.setTint(
            DrawableCompat.wrap(activityShowBook.background),
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        DrawableCompat.setTint(
            DrawableCompat.wrap(showBookToolbar.background),
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
    }

    /**
     * Setup the view of thr current book.
     */
    private fun setUp() {
        appBarShowBookTvTitle.text = Constants.CURRENT_BOOK.title
        activityShowBookTvAuthorAux.text = Constants.CURRENT_BOOK.author
        activityShowBookTvGenreAux.text = Constants.CURRENT_BOOK.genre
        activityShowBookTvSynopsisAux.text= Constants.CURRENT_BOOK.synopsis
        if (!Constants.CURRENT_BOOK.premium)
            activityShowBookTvPremiumAux.text = getString(R.string.free)

        //setup backgound, button and toolbar colors
        when (Constants.CURRENT_BOOK.genre?.toLowerCase()) {
            "adventure" -> {
                DrawableCompat.setTint(
                    DrawableCompat.wrap(activityShowBook.background),
                    ContextCompat.getColor(this, R.color.gold)
                );
                DrawableCompat.setTint(
                    DrawableCompat.wrap(showBookToolbar.background),
                    ContextCompat.getColor(this, R.color.gold)
                );
                activityShowBookBtnRead.setBackgroundResource(R.drawable.container_round_gold)
            }
            "terror" -> {
                DrawableCompat.setTint(
                    DrawableCompat.wrap(activityShowBook.background),
                    ContextCompat.getColor(this, R.color.darkPurple)
                );
                DrawableCompat.setTint(
                    DrawableCompat.wrap(showBookToolbar.background),
                    ContextCompat.getColor(this, R.color.darkPurple)
                );
                activityShowBookBtnRead.setBackgroundResource(R.drawable.container_round_purple)
                appBarShowBookTvTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            "sci-fy" -> {
                DrawableCompat.setTint(
                    DrawableCompat.wrap(activityShowBook.background),
                    ContextCompat.getColor(this, R.color.colorPrimaryDark)
                );
                DrawableCompat.setTint(
                    DrawableCompat.wrap(showBookToolbar.background),
                    ContextCompat.getColor(this, R.color.colorPrimaryDark)
                );
                activityShowBookBtnRead.setBackgroundResource(R.drawable.container_round_orange)
            }
            else -> {
                DrawableCompat.setTint(
                    DrawableCompat.wrap(activityShowBook.background),
                    ContextCompat.getColor(this, R.color.blue)
                );
                DrawableCompat.setTint(
                    DrawableCompat.wrap(showBookToolbar.background),
                    ContextCompat.getColor(this, R.color.blue)
                );
                activityShowBookBtnRead.setBackgroundResource(R.drawable.container_round_blue)
            }
        }
    }

    /**
     * Finish the activity
     *
     * @param view
     */
    fun previousActivity(view: View) {
        finish()
    }
}
