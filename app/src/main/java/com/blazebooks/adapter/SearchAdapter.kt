package com.blazebooks.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.model.Book
import com.blazebooks.ui.books.ShowBookActivity

class SearchAdapter(private var itemList: List<Book>, private val activity: Context) :
    RecyclerView.Adapter<SearchAdapter.CustomViewHolder>() {

    /**
     * Contains the book view data. Implements RecyclerView.ViewHolder.
     *
     * @param view
     */
    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleText: TextView = view.findViewById(R.id.activitySearchBookName)
        var genreText: TextView = view.findViewById(R.id.activitySearchBookGenreAux)
        var premiumText: TextView = view.findViewById(R.id.activitySearchBookPremium)
        var itemLayout: ConstraintLayout = view.findViewById(R.id.bookSearchItemCl)

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
     */
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleText.text = item.title
        holder.genreText.text = item.genre

        if (item.premium) {
            holder.premiumText.text = activity.getString(R.string.premium)
            holder.itemLayout.apply {
                this.setBackgroundResource(R.drawable.container_round_gold)
                this.setPadding(60, 60, 60, 60)
            }
        } else {
            holder.premiumText.text = activity.getString(R.string.free)
            holder.itemLayout.apply {
                this.setBackgroundResource(R.drawable.container_round_blue)
                this.setPadding(60, 60, 60, 60)
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
     */
    fun updateList(list: MutableList<Book>) {
        itemList = list
        notifyDataSetChanged()
    }
}