package com.jarvis.myleague.ui.detail_team

import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.jarvis.design_system.textview.CustomTextView
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.myleague.R
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.databinding.ActivityDetailTeamBinding
import com.jarvis.myleague.ui.base.BaseActivity
import com.jarvis.myleague.ui.base.adapter.SimpleListAdapter
import com.jarvis.myleague.ui.core.StatusMatches
import com.jarvis.myleague.ui.pref.AppPreferenceKey
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailTeamActivity :
    BaseActivity<ActivityDetailTeamBinding, DetailTeamViewModel>(ActivityDetailTeamBinding::inflate) {
    private val viewModel: DetailTeamViewModel by viewModel()

    private val adapter5Matches by lazy {
        SimpleListAdapter<Int>(R.layout.item_matches) { itemView, item, _ ->
            val icLogo = itemView.findViewById<AppCompatImageView>(R.id.icLogo)
            val resource = when (item) {
                StatusMatches.WIN.value -> R.drawable.ic_win
                StatusMatches.DRAW.value -> R.drawable.ic_draw
                StatusMatches.LOSE.value -> R.drawable.ic_lose
                else -> R.drawable.ic_null_match
            }
            icLogo.setImageResource(resource)
        }
    }

    private val adapterDetail by lazy {
        SimpleListAdapter<Matches>(R.layout.item_match_detail) { itemView, item, _ ->
            val tvHome = itemView.findViewById<CustomTextView>(R.id.tvHome)
            val tvAway = itemView.findViewById<CustomTextView>(R.id.tvAway)
            val etScoreHome = itemView.findViewById<CustomTextView>(R.id.etScoreHome)
            val etScoreAway = itemView.findViewById<CustomTextView>(R.id.etScoreAway)

            tvHome.text = item.teamHome
            tvAway.text = item.teamAway
            etScoreHome.text = if (item.scoreHome == null) "" else item.scoreHome.toString()
            etScoreAway.text = if (item.scoreAway == null) "" else item.scoreAway.toString()
        }
    }

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun setUpViews() {
        super.setUpViews()
        initIntent()
        initView()
    }

    private fun initIntent() {
        viewModel.idTeam = intent.getLongExtra(AppPreferenceKey.ID_TEAM_CREATE, 0L)
        viewModel.getTeamToId()
        viewModel.getDetailTeamToId()
    }

    private fun initView() {
        binding.rc5Matches.adapter = adapter5Matches
        binding.rcDetail.adapter = adapterDetail
    }

    override fun observeData() {
        super.observeData()
        observe(viewModel.listMatches) { list ->
            adapterDetail.submitList(list)

            val list5Matches = list.map { it.copy() }.filterIndexed { index, _ ->
                index < 5
            }.map {
                checkStatusMatch(viewModel.idTeam, it)
            }
            adapter5Matches.submitList(list5Matches)
        }

        observe(viewModel.teamModel) {
            binding.tvDraw.text = getString(R.string.draw_, it.draw.toString())
            binding.tvLose.text = getString(R.string.loses_, it.lost.toString())
            binding.tvWins.text = getString(R.string.wins_, it.win.toString())
            binding.tvGoals.text = getString(R.string.goals_, it.bt.toString())
            binding.tvConceded.text = getString(R.string.conceded_, it.sbt.toString())
            binding.toolbar.setTitle(it.name)
            binding.icLogo.setImageResource(
                if (it.logo != 0) it.logo ?: 0 else R.drawable.ic_security_black
            )
        }

        observe(viewModel.error) {
            Toast.makeText(this, "Load data fail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkStatusMatch(idTeam: Long, matches: Matches): Int {
        return if (idTeam == matches.idTeamHome)
            when {
                matches.scoreAway == null || matches.scoreHome == null -> StatusMatches.NULL.value
                matches.scoreHome!! < matches.scoreAway!! -> StatusMatches.LOSE.value
                matches.scoreHome == matches.scoreAway -> StatusMatches.DRAW.value
                else -> StatusMatches.WIN.value
            }
        else
            when {
                matches.scoreAway == null || matches.scoreHome == null -> StatusMatches.NULL.value
                matches.scoreAway!! < matches.scoreHome!! -> StatusMatches.LOSE.value
                matches.scoreAway == matches.scoreHome -> StatusMatches.DRAW.value
                else -> StatusMatches.WIN.value
            }
    }

}
