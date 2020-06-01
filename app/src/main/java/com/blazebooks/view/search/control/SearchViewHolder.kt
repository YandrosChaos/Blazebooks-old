package com.blazebooks.view.search.control

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R

/**
 * Contains the book view data. Implements RecyclerView.ViewHolder.
 *
 * @param view
 * @see SearchAdapter
 * @author Victor Gonzalez
 */
class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var titleText: TextView = view.findViewById(R.id.searchBookItemText)
    var bookImage: ImageView = view.findViewById(R.id.searchBookItemImg)
    var bookPremiumImg: ImageView = view.findViewById(R.id.searchBookItemImgPremium)
    var itemLayout: CardView = view.findViewById(R.id.bookSearchItemCv)

}