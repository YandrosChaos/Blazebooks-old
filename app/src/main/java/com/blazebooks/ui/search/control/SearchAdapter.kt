package com.blazebooks.ui.search.control

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.blazebooks.util.Constants
import com.blazebooks.R
import com.blazebooks.model.Book
import com.blazebooks.ui.showbook.ShowBookActivity

/**
 * @see ShowBookActivity
 * @author Victor Gonzalez
 */
class SearchAdapter(private var itemList: List<Book>, private val activity: Context) :
    RecyclerView.Adapter<SearchViewHolder>() {

    /**
     * Inflates the layout.
     *
     * @see SearchViewHolder
     *
     * @param parent
     * @param viewType
     *
     * @return CustomViewHolder
     *
     * @author Victor Gonzalez
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_book, parent, false)
        return SearchViewHolder(view)
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
     * @see SearchViewHolder
     * @see Book
     * @see ShowBookActivity
     * @author Victor Gonzalez
     */
    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = itemList[position]

        //set title with first letter in UpperCase
        item.title = item.title!!.capitalize()
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

        //sort by chapter number
        item.chapters.sortBy { chapter -> chapter.number }

        //set click listener
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