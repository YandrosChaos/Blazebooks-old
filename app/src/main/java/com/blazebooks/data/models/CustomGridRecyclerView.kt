package com.blazebooks.data.models

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Custom RecyclerView. Allows animations to a GridLayout.
 *
 * @author Victor Gonzalez
 */
class CustomGridRecyclerView : RecyclerView {

    //constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            : super(context, attrs, defStyle)

    override fun setLayoutManager(layout: LayoutManager?) {
        if (layout is GridLayoutManager) {
            super.setLayoutManager(layout)
        } else {
            throw ClassCastException("This recyclerview should use GridLayoutManager as layout manager")
        }
    }

    /**
     * Configures custom animation parameters.
     *
     * @Victor Gonzales
     */
    override fun attachLayoutAnimationParameters(
        child: View,
        params: ViewGroup.LayoutParams,
        index: Int,
        count: Int
    ) {
        if (adapter != null && layoutManager is GridLayoutManager) {
            var animationParams =
                params.layoutAnimationParameters as? GridLayoutAnimationController.AnimationParameters
            val columns = (layoutManager as GridLayoutManager?)!!.spanCount
            val invertedIndex = count - 1 - index

            if (animationParams == null) {
                animationParams = GridLayoutAnimationController.AnimationParameters()
                params.layoutAnimationParameters = animationParams
            }

            animationParams.apply {
                this.count = count
                this.index = index
                this.columnsCount = columns
                this.rowsCount = count / columns
                this.column = columns - 1 - invertedIndex % columns
                this.row = this.rowsCount - 1 - invertedIndex / columns
            }
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }

}