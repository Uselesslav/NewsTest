package uselesslav.newstest.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import uselesslav.newstest.R

/**
 * Делитель элементов списка
 */
class SimpleDividerItemDecoration(context: Context, private val mDrawTopDivider: Boolean) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable?

    init {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top: Int
            if (mDrawTopDivider) {
                top = child.top
            } else {
                top = child.bottom + params.bottomMargin
            }
            val bottom = top + mDivider!!.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}