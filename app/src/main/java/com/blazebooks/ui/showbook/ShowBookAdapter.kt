package com.blazebooks.ui.showbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.model.Chapter
import kotlinx.android.synthetic.main.activity_show_book_item_chapter.view.*

/**
 * @see com.blazebooks.ui.showbook.ShowBookActivity
 * @author Victor González
 */
class ShowBookAdapter(private val chapterList: ArrayList<Chapter>) :
    RecyclerView.Adapter<ShowBookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_show_book_item_chapter, parent, false)

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

    inner class BookViewHolder(itemView: View) :
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
            //if (chapter.image != null)
            //itemView.showChapterImageView.setImageDrawable(chapter.image)
            itemView.showChapterTvNumberAux.text = chapter.number.toString()
            itemView.showChapterTvTitleAux.text = chapter.title

            if (chapter.readed)
                itemView.showChapterIvReaded.setBackgroundResource(R.drawable.ic_check)

        }
    }
}