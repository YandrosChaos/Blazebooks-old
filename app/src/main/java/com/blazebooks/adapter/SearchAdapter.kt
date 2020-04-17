package com.blazebooks.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.model.Book
import com.blazebooks.ui.showbook.ShowBookActivity

/**
 * @see ShowBookActivity
 * @author Victor Gonzalez
 */
class SearchAdapter(private var itemList: List<Book>, private val activity: Context) :
    RecyclerView.Adapter<SearchAdapter.CustomViewHolder>() {

    /**
     * Contains the book view data. Implements RecyclerView.ViewHolder.
     *
     * @param view
     * @author Victor Gonzalez
     */
    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleText: TextView = view.findViewById(R.id.searchBookItemText)
        var bookImage: ImageView = view.findViewById(R.id.searchBookItemImg)
        var bookPremiumImg: ImageView = view.findViewById(R.id.searchBookItemImgPremium)
        var itemLayout: CardView = view.findViewById(R.id.bookSearchItemCv)

    }

    /**
     * Inflates the layout.
     *
     * @see CustomViewHolder
     *
     * @param parent
     * @param viewType
     *
     * @return CustomViewHolder
     *
     * @author Victor Gonzalez
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_search_book_item, parent, false)
        return CustomViewHolder(view)
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
     * <p>The click listener starts a new activity when is clicked. This new activity shows
     * more information about the book.</p>
     *
     * @param holder
     * @param position
     *
     * @see CustomViewHolder
     * @see Book
     * @see ShowBookActivity
     * @author Victor Gonzalez
     */
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleText.text = item.title

        //set image
        if (item.image != null) {
            holder.bookImage.load(item.image) {
                crossfade(true)
            }
        } else {
            //default image
            holder.bookImage.load(R.drawable.blazebooks_logo) {
                crossfade(true)
            }
        }

        //set premium image
        if (item.premium) {
            holder.bookPremiumImg.apply {
                this.visibility = View.VISIBLE
            }
        } else {
            holder.bookPremiumImg.apply {
                this.visibility = View.GONE
            }
        }

        holder.itemLayout.setOnClickListener {
            Constants.CURRENT_BOOK = item
            activity.startActivity(Intent(activity, ShowBookActivity::class.java))
        }

    }

    /**
     * Updates the adapter list with a new given list and
     * notify that the dataset changed.
     *
     * @param list
     * @author Victor Gonzalez
     */
    fun updateList(list: MutableList<Book>) {
        itemList = list
        notifyDataSetChanged()
    }
}