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

    private val divider: Drawable? = ContextCompat.getDrawable(context, R.drawable.divider)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top: Int
            top = if (mDrawTopDivider) {
                child.top
            } else {
                child.bottom + params.bottomMargin
            }
            val bottom = top + divider!!.intrinsicHeight

            divider.setBounds(
                    parent.paddingLeft,
                    top,
                    parent.width - parent.paddingRight,
                    bottom)
            divider.draw(c)
        }
    }
}