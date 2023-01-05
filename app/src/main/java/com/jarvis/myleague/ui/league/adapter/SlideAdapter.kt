@file:Suppress("DEPRECATION")

package com.jarvis.myleague.ui.league.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SlideAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    private var mCurrentPosition = -1
    private var listFragment: MutableList<Fragment> = ArrayList()

    fun addData(listFragment: List<Fragment>?) {
        if (listFragment != null) {
            this.listFragment.clear()
            this.listFragment.addAll(listFragment)
            notifyDataSetChanged()
        }
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }


    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != mCurrentPosition && container is CustomViewPager) {
            val fragment = `object` as Fragment?
            if (fragment != null && fragment.view != null) {
                mCurrentPosition = position
                container.measureCurrentView(fragment.view)
            }
        }
    }

}
