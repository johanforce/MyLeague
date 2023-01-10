package com.jarvis.myleague.ui.create

import android.content.Intent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.jarvis.design_system.textview.CustomTextView
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.myleague.R
import com.jarvis.myleague.common.click
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.databinding.ActivityGameBinding
import com.jarvis.myleague.ui.backets.BracketsActivity
import com.jarvis.myleague.ui.base.BaseActivity
import com.jarvis.myleague.ui.base.adapter.SimpleListAdapter
import com.jarvis.myleague.ui.core.TypeLeagueEnum
import com.jarvis.myleague.ui.league.LeagueActivity
import com.jarvis.myleague.ui.pref.AppPreferenceKey
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateTeamActivity :
    BaseActivity<ActivityGameBinding, CreateTeamViewModel>(ActivityGameBinding::inflate) {
    private val viewModel: CreateTeamViewModel by viewModel()

    private val adapter by lazy {
        SimpleListAdapter<String>(R.layout.item_select_create) { itemView, item, position ->
            val tvName = itemView.findViewById<CustomTextView>(R.id.tvName)
            val tvSTT = itemView.findViewById<CustomTextView>(R.id.tvStt)
            tvName.text = item
            tvSTT.text = (position + 1).toString()
        }
    }

    override fun getToolbar(): JxToolbar? {
        return binding.toolbar
    }

    override fun setUpViews() {
        super.setUpViews()
        initIntent()
        initView()
        setOnClickView()
    }

    private fun initIntent() {
        viewModel.getLeagueToId(intent.getLongExtra(AppPreferenceKey.ID_LEAGUE_CREATE, 0L))
    }

    private fun setOnClickView() {
        binding.btAdd.click {
            addTeam()
        }
        binding.etTeam.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTeam()
            }
            true
        }

        binding.btCreate.click {
            if (viewModel.listTeam.isEmpty() || viewModel.listTeam.size < 3) {
                Toast.makeText(this, getString(R.string.please_add_team), Toast.LENGTH_SHORT).show()
                return@click
            }
            if (viewModel.listTeam.size == 20) {
                Toast.makeText(
                    this,
                    getString(R.string.out_of_limit_team),
                    Toast.LENGTH_SHORT
                ).show()
                return@click
            }
            viewModel.createData()
        }
    }

    private fun addTeam() {
        val textData = binding.etTeam.text.toString()
        if (viewModel.listTeam.find { it == textData }?.isNotEmpty() == true) return
        if (viewModel.listTeam.size > (viewModel.leagueCreate.teams
                ?: 0) && viewModel.leagueCreate.type == TypeLeagueEnum.CUP.value
        ) return
        if (textData.isNotEmpty()) {
            viewModel.listTeam.add(textData)
            adapter.submitList(viewModel.listTeam)
        }
        binding.etTeam.setText("")
    }

    private fun initView() {
        binding.rcvMyMedication.adapter = adapter
        adapter.onItemClick = { itemView, item, _ ->
            viewModel.listTeam.remove(item)
            adapter.submitList(viewModel.listTeam)
        }
        binding.etTeam.imeOptions = EditorInfo.IME_ACTION_DONE
    }

    override fun observeData() {
        super.observeData()
        observe(viewModel.isCreateSuccess) {
            if (viewModel.leagueCreate.type == TypeLeagueEnum.LEAGUE.value) {
                viewModel.createMatches()
            } else {
                viewModel.createMatchesCup()
            }
        }

        observe(viewModel.isCreateMatches) {
            Toast.makeText(this, "Add thành công", Toast.LENGTH_SHORT).show()
            if (viewModel.leagueCreate.type == TypeLeagueEnum.LEAGUE.value) {
                val intent = Intent(this, LeagueActivity::class.java)
                intent.putExtra(AppPreferenceKey.ID_LEAGUE_CREATE, viewModel.leagueCreate.id)
                startActivity(intent)
            } else {
                val intent = Intent(this, BracketsActivity::class.java)
                intent.putExtra(AppPreferenceKey.ID_LEAGUE_CREATE, viewModel.leagueCreate.id)
                startActivity(intent)
            }
            finish()
        }

        observe(viewModel.error) {
            Toast.makeText(this, "Add thất bại", Toast.LENGTH_SHORT).show()
        }
    }

}
