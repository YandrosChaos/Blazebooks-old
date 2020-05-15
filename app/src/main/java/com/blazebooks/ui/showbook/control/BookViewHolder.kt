package com.blazebooks.ui.showbook.control

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.model.Chapter
import kotlinx.android.synthetic.main.item_show_book_chapter.view.*

class BookViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    /**
     * Setup item view and onClickListener
     *
     * @param chapter
     */
    fun bindChapter(chapter: Chapter) {

        itemView.setOnClickListener {
        }

        //cargar los datos del libro para mostrarlos
        itemView.showChapterTvNumberAux.text = chapter.number.toString()
        itemView.showChapterTvTitleAux.text = chapter.title

        if (chapter.readed)
            itemView.showChapterIvReaded.setBackgroundResource(R.drawable.ic_check)

    }
}