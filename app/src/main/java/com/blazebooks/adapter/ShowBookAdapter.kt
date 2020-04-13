package com.blazebooks.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.model.Chapter
import kotlinx.android.synthetic.main.activity_show_book_item_chapter.view.*

class ShowBookAdapter(private val chapterList: ArrayList<Chapter>) :
    RecyclerView.Adapter<ShowBookAdapter.WorkoutViewHolder>() {

    /**
     * Este es el método que crea la vista
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_show_book_item_chapter, parent, false)

        return WorkoutViewHolder(layoutInflate)

    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
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

    inner class WorkoutViewHolder(itemView: View) :
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