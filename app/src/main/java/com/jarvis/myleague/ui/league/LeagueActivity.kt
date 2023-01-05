@file:Suppress("DEPRECATION")

package com.jarvis.myleague.ui.league

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.jarvis.myleague.ui.league.adapter.SlideAdapter
import com.jarvis.myleague.databinding.ActivityLeagueBinding
import com.jarvis.myleague.ui.base.BaseActivity

class LeagueActivity :
    BaseActivity<ActivityLeagueBinding, LeagueViewModel>(
        ActivityLeagueBinding::inflate
    ) {

    private val viewModel: LeagueViewModel by viewModels()

    var tabIndex = 0
    private val fragmentLeague = 0
    private val fragmentFixtures = 1
    private var tabLeague: TabLayout.Tab? = null
    private var tabFixtures: TabLayout.Tab? = null
    private val fragments: MutableList<Fragment> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

//    override fun getToolbar(): JxToolbar {
//        return binding.toolbar
//    }

    override fun setUpViews() {
        super.setUpViews()

        initData()
    }

    private fun initData() {
        setupViewPager()
        setOnClickListener()
    }

    private fun setupViewPager() {
        val leagueFragment = LeagueFragment()
        val fixturesFragment = FixturesFragment()
        this.fragments.add(leagueFragment)
        this.fragments.add(fixturesFragment)
        binding.viewPager.offscreenPageLimit = 2
        val adapter = SlideAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        adapter.addData(fragments)
        binding.viewPager.adapter = adapter
        this.tabLeague =
            binding.viewTab.getTabAt(fragmentLeague)
        this.tabFixtures =
            binding.viewTab.getTabAt(fragmentFixtures)
        binding.viewPager.currentItem = this.tabIndex
        setupTabLayout()
    }

    fun setupTabLayout() {
        when (tabIndex) {
            fragmentLeague -> {
                binding.viewTab.selectTab(tabLeague)
            }
            else -> {
                binding.viewTab.selectTab(tabFixtures)
            }
        }
    }

    private fun setOnClickListener() {
        binding.viewTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //do nothing
            }
        })
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //do nothing
            }

            override fun onPageSelected(position: Int) {
                tabIndex = position
                setupTabLayout()
            }

            override fun onPageScrollStateChanged(state: Int) {
                //do nothing
            }
        })
    }
}



