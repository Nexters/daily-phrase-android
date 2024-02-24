package com.silvertown.android.dailyphrase.presentation.ui.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.silvertown.android.dailyphrase.presentation.extensions.dpToPx

class PostItemDecoration(val context: Context) : RecyclerView.ItemDecoration() {
    private val dividerHeight = 1.dpToPx(context)
    private val dividerMargin = 16.dpToPx(context)
    private val headerMargin = 8.dpToPx(context)
    private val footerMargin = 16.dpToPx(context)
    private val dividerColor = 0xFFF2F3F6.toInt() // color.xml 생기면 수정

    private val paint: Paint = Paint()

    init {
        paint.color = dividerColor
        paint.isAntiAlias = true
    }

    override fun getItemOffsets(
        outRect: android.graphics.Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        when (position) {
            0 -> {
                outRect.top = headerMargin
            }

            itemCount - 1 -> {
                outRect.bottom = footerMargin
            }

            else -> {
                outRect.bottom = dividerHeight
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + dividerMargin
        val right = parent.width - parent.paddingRight - dividerMargin

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerHeight

            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}
