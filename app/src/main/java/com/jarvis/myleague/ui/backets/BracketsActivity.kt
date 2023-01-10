package com.jarvis.myleague.ui.backets

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.design_system.toolbar.OnToolbarActionListener
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.databinding.ActivityBracketsTeamBinding
import com.jarvis.myleague.ui.base.BaseActivity
import com.jarvis.myleague.ui.core.Brackets
import com.jarvis.myleague.ui.league.TeamFragment
import com.jarvis.myleague.ui.league.adapter.SlideAdapter
import com.jarvis.myleague.ui.pref.AppPreferenceKey
import org.koin.androidx.viewmodel.ext.android.viewModel


class BracketsActivity :
    BaseActivity<ActivityBracketsTeamBinding, BracketsViewModel>(ActivityBracketsTeamBinding::inflate),
    OnToolbarActionListener {
    private val viewModel: BracketsViewModel by viewModel()

    var idLeague = 0L

    var tabIndex = 0
    private val fragmentResults = 0
    private val fragmentTeam = 1
    private var tabResult: TabLayout.Tab? = null
    private var tabTeam: TabLayout.Tab? = null
    private val fragments: MutableList<Fragment> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun setUpViews() {
        super.setUpViews()

        initData()
    }

    private fun initData() {
        idLeague = intent?.getLongExtra(AppPreferenceKey.ID_LEAGUE_CREATE, 0L) ?: 0L
        this.binding.toolbar.setOnToolbarActiontListener(this)
        setupViewPager()
        setOnClickListener()
    }

    private fun setupViewPager() {
        val resultsFragment = ResultsFragment()
        val teamFragment = TeamFragment()
        this.fragments.add(resultsFragment)
        this.fragments.add(teamFragment)
        binding.viewPager.offscreenPageLimit = 2
        val adapter = SlideAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        adapter.addData(fragments)
        binding.viewPager.adapter = adapter
        this.tabResult =
            binding.viewTab.getTabAt(fragmentResults)
        this.tabTeam =
            binding.viewTab.getTabAt(fragmentTeam)
        binding.viewPager.currentItem = this.tabIndex
        setupTabLayout()
    }

    fun setupTabLayout() {
        when (tabIndex) {
            fragmentTeam -> {
                binding.viewTab.selectTab(tabTeam)
            }
            else -> {
                binding.viewTab.selectTab(tabResult)
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
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

    override fun observeData() {
        super.observeData()
        observe(viewModel.isLoadDataSuccess) {
            viewModel.isLoadDataSuccess.value = null
            if (it) {
                val data = Brackets.showBracketsCup(viewModel.listMatches)
                if (data.isEmpty()) return@observe
                appPreference?.put(AppPreferenceKey.KEY_DATA, BracketsModel(data))
            }
        }
    }

    override fun onToolbarTextCtaClick() {
        TODO("Not yet implemented")
    }

    override fun onToolbarAction1Click() {
        TODO("Not yet implemented")
    }

    override fun onToolbarAction2Click() {
        viewModel.getData(idLeague)
        val intent = Intent(this, BracketsMapActivity::class.java)
        startActivity(intent)
    }

}
