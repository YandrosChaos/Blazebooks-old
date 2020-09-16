package com.blazebooks.ui.showbook.chapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.data.models.Chapter
import com.blazebooks.ui.reader.ReaderActivity
import kotlinx.android.synthetic.main.item_show_book_chapter.view.*

class ChapterViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    /**
     * Setup item view and onClickListener
     *
     * @param chapter
     */
    fun bindChapter(chapter: Chapter) {

        //Abre el libro por la pagina del capitulo seleccionado
        itemView.setOnClickListener {
            itemView.context.startActivity(Intent(itemView.context, ReaderActivity::class.java).putExtra("CHAPTER", Integer.parseInt(itemView.showChapter_number.text.toString())))
        }

        //cargar los datos del libro para mostrarlos
        itemView.showChapter_number.text = chapter.number.toString()
        itemView.showChapter_title.text = chapter.title

        if (chapter.readed)
            itemView.showChapterIvReaded.setBackgroundResource(R.drawable.ic_check)
    }

}