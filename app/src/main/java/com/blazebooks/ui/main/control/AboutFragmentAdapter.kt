package com.blazebooks.ui.main.control

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.blazebooks.R

class AboutFragmentAdapter(
    private var itemList: List<Pair<String, Int>>,
    private val activity: Context
) :
    RecyclerView.Adapter<AboutViewHolder>() {

    /**
     * Inflates the layout.
     *
     * @see AboutViewHolder
     *
     * @param parent
     * @param viewType
     *
     * @return CustomViewHolder
     *
     * @author Victor Gonzalez
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fragment_about_card, parent, false)
        return AboutViewHolder(view)
    }

    /**
     * Returns the list size
     *
     * @return Int
     * @author Victor Gonzalez
     */
    override fun getItemCount(): Int {
        return itemList.size
    }

    /**
     * <p>Sets the view and the click listener with the item data. Sets a different view for
     * a PREMIUM or a FREE book. </p>
     * <p>The click listener starts a new dialog when is clicked. This new dialog shows
     * more information about the book.</p>
     *
     * @param holder
     * @param position
     *
     * @see AboutFragmentDialogBuilder
     *
     * @author Victor Gonzalez
     */
    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        val item = itemList[position]
        holder.cardTitle.text = item.first
        holder.imageViewSrc.load(item.second)

        val dialog = AboutFragmentDialogBuilder(activity, item.first).createAlertDialog()

        //set click listener
        holder.layout.setOnClickListener {
            dialog.show()

        }
    }
}