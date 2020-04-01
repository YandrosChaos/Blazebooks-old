package com.blazebooks.ui.books

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import com.blazebooks.Constants
import com.blazebooks.R
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.app_bar_show_book.*

class ShowBookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_book)
        setUp()
        activityShowBookChapters.layoutManager = LinearLayoutManager(this)
        activityShowBookChapters.adapter = ShowBookAdapter(Constants.CURRENT_BOOK.chapters, this)
    }

    private fun setUp() {
        appBarShowBookTvTitle.text = Constants.CURRENT_BOOK.title
        activityShowBookTvAuthorAux.text = Constants.CURRENT_BOOK.author
        activityShowBookTvGenreAux.text = Constants.CURRENT_BOOK.genre
        if (!Constants.CURRENT_BOOK.premium)
            activityShowBookTvPremiumAux.text = getString(R.string.free)

        when (Constants.CURRENT_BOOK.genre.toLowerCase()) {
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

    fun previousActivity(view: View) {
        finish()
    }
}
