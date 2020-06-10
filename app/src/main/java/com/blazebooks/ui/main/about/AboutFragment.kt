package com.blazebooks.ui.main.about

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.data.models.CustomGridRecyclerView

/**
 * About fragment. This fragment is dynamically generated using arrays.xml resources.
 *
 * @author Victor González
 */
class AboutFragment : Fragment() {

    private lateinit var mRecycler: CustomGridRecyclerView
    private lateinit var mAdapter: AboutFragmentAdapter
    private lateinit var itemList: ArrayList<Pair<String, Int>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_about, container, false)

        itemList = getResourceList()
        setUpUI(view)

        return view
    }

    /**
     * Runs the custom recyclerView animation.
     *
     * @see CustomGridRecyclerView
     * @see AboutFragmentAdapter
     *
     * @author Victor Gonzalez
     */
    private fun runRecyclerViewAnimation() {
        mAdapter.notifyDataSetChanged()
        mRecycler.scheduleLayoutAnimation()
    }

    /**
     * Configures the adapter, layout manager and animation of main view.
     *
     * @author Victor González
     */
    private fun setUpUI(view: View) {
        mRecycler = view.findViewById(R.id.recyclerView_aboutFragment)
        mAdapter = AboutFragmentAdapter(
            itemList,
            requireActivity()
        )
        mRecycler.layoutManager =
            GridLayoutManager(
                requireContext(),
                2,
                RecyclerView.VERTICAL,
                false
            )
        mRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.gridlayout_animation_from_bottom
            )
        mRecycler.adapter = mAdapter
    }

    /**
     * Loads arrays.xml stored data into list and return it.
     *
     * @return ArrayList<Pair<String, Int>> with titles and drawable ids
     * @author Victor González
     */
    @SuppressLint("Recycle")
    private fun getResourceList(): ArrayList<Pair<String, Int>> {
        val tempItemList = arrayListOf<Pair<String, Int>>()
        val titles = resources.getStringArray(R.array.about_us_titles)
        val images = resources.obtainTypedArray(R.array.about_us_drawables)

        for (x in titles.indices) {
            tempItemList.add(Pair(titles[x], images.getResourceId(x, 0)))
        }

        return tempItemList
    }
}
