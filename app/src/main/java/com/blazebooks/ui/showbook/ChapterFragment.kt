package com.blazebooks.ui.showbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.adapter.ShowBookAdapter
import kotlinx.android.synthetic.main.fragment_show_book_chapters.*
import kotlinx.android.synthetic.main.fragment_show_book_chapters.view.*
import kotlinx.android.synthetic.main.fragment_show_book_chapters.view.activityShowBookChapters

class ChapterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_book_chapters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.activityShowBookChapters)
        recyclerView.adapter = ShowBookAdapter(Constants.CURRENT_BOOK.chapters)
        recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }
}