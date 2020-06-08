package com.blazebooks.ui.showbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.ui.showbook.control.ShowBookAdapter
import com.blazebooks.util.CURRENT_BOOK

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
        recyclerView.adapter =
            ShowBookAdapter(CURRENT_BOOK.chapters)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
    }
}