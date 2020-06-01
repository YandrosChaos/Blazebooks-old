package com.blazebooks.view.showbook.control

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.model.Chapter

/**
 * @see com.blazebooks.view.showbook.ShowBookActivity
 * @author Victor González
 */
class ShowBookAdapter(private val chapterList: ArrayList<Chapter>) :
    RecyclerView.Adapter<BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show_book_chapter, parent, false)

        return BookViewHolder(layoutInflate)

    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = chapterList[position]
        holder.bindChapter(item)
    }

    /**
     * Devuelve la posición del elemento a onCreateViewHolder. De esta forma se
     * puede conocer qué layout cargar según la posición que ocupe
     */
    override fun getItemViewType(position: Int): Int {
        return position
    }

}