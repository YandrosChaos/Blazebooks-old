package com.blazebooks.ui.showbook.chapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.data.models.Chapter


/**
 * @author Victor González
 */
class ChapterAdapter(private val chapterList: ArrayList<Chapter>) :
    RecyclerView.Adapter<ChapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show_book_chapter, parent, false)

        return ChapterViewHolder(layoutInflate)

    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
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