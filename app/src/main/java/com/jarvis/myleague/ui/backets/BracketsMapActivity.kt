package com.jarvis.myleague.ui.backets

import android.os.Bundle
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.myleague.databinding.ActivityBracketsMapBinding
import com.jarvis.myleague.ui.base.BaseActivity
import com.jarvis.myleague.ui.pref.AppPreferenceKey
import org.koin.androidx.viewmodel.ext.android.viewModel


class BracketsMapActivity :
    BaseActivity<ActivityBracketsMapBinding, BracketsMapViewModel>(ActivityBracketsMapBinding::inflate) {
    private val viewModel: BracketsMapViewModel by viewModel()


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
        val data = appPreference?.get(AppPreferenceKey.KEY_DATA, BracketsModel::class.java)
        binding.bracketView.setBracketsData(data?.data)
    }
}