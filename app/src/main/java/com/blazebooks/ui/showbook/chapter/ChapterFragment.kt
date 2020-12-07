package com.blazebooks.ui.showbook.chapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blazebooks.R
import com.blazebooks.util.CURRENT_BOOK
import kotlinx.android.synthetic.main.fragment_show_book_chapters.*


class ChapterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_book_chapters, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityShowBookChapters.adapter = ChapterAdapter(CURRENT_BOOK.chapters)
        activityShowBookChapters.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)

    }


}