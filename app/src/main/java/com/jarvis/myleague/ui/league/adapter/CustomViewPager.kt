@file:Suppress("NAME_SHADOWING")

package com.jarvis.myleague.ui.league.adapter

import android.content.Context
import android.view.View
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context) : ViewPager(context) {
    private var mCurrentView: View? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mCurrentView != null) {
            mCurrentView!!.measure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            val height = 0.coerceAtLeast(mCurrentView!!.measuredHeight)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun measureCurrentView(currentView: View?) {
        mCurrentView = currentView
        requestLayout()
    }
}
