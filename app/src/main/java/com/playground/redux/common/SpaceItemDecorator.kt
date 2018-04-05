package com.playground.redux.common

import android.graphics.Rect
import android.support.v7.widget.RecyclerView

class SpaceItemDecorator(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = space
        outRect.left = space
        outRect.right = space
        if (parent.getChildAdapterPosition(view) != parent.adapter.itemCount - 1) {
            outRect.bottom = space
        }
    }
}