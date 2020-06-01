package com.blazebooks.view.main.control

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R

class AboutViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    var cardTitle: TextView = view.findViewById(R.id.aboutFragmentItemTitle)
    var imageViewSrc: ImageView = view.findViewById(R.id.aboutFragmentItemIv)
    var layout : LinearLayout = view.findViewById(R.id.aboutFragmentItemLayout)

}