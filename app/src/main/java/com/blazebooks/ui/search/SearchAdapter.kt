package com.blazebooks.ui.search

import com.blazebooks.model.Book

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import kotlinx.android.synthetic.main.activity_search_book_item.view.*

class SearchAdapter(private val booksList: ArrayList<Book>, val activity: Context) :
    RecyclerView.Adapter<SearchAdapter.WorkoutViewHolder>() {

    /**
     * Este es el método que crea la vista
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_search_book_item, parent, false)

        return WorkoutViewHolder(layoutInflate)

    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val itemRanking = booksList[position]
        holder.bindRanking(itemRanking)
    }

    /**
     * Devuelve la posición del elemento a onCreateViewHolder. De esta forma se
     * puede conocer qué layout cargar según la posición que ocupe
     */
    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class WorkoutViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindRanking(book: Book) {

            itemView.setOnClickListener {
            }
            //cargar los datos del registro para mostrarlos
            itemView.activitySearchBookName.text = book.title
            itemView.activitySearchBookGenreAux.text = book.genre

            if (book.premium) {
                itemView.activitySearchBookPremium.text = activity.getString(R.string.premium)
            } else {
                itemView.activitySearchBookPremium.text = activity.getString(R.string.free)
            }

            when (book.genre.toLowerCase()) {
                "adventure" -> {
                    itemView.bookSearchItemCl.setBackgroundResource(R.drawable.container_round_gold)
                    itemView.bookSearchItemCl.setPadding(60,60,60,60)
                }
                "terror" -> {
                    itemView.bookSearchItemCl.setBackgroundResource(R.drawable.container_round_purple)
                    itemView.bookSearchItemCl.setPadding(60,60,60,60)
                }
                "sci-fy" -> {
                    itemView.bookSearchItemCl.setBackgroundResource(R.drawable.container_round_orange)
                    itemView.bookSearchItemCl.setPadding(60,60,60,60)
                }
            }


        }
    }
}