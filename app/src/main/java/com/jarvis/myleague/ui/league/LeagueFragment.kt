package com.jarvis.myleague.ui.league

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.jarvis.design_system.textview.CustomTextView
import com.jarvis.myleague.R
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.databinding.FragmentLeagueBinding
import com.jarvis.myleague.ui.base.BaseFragment
import com.jarvis.myleague.ui.base.adapter.SimpleListAdapter
import com.jarvis.myleague.ui.pref.AppPreferenceKey

@SuppressLint("SetTextI18n")
class LeagueFragment :
    BaseFragment<FragmentLeagueBinding>(FragmentLeagueBinding::inflate) {

    private val viewModel: TableViewModel by viewModels()
    private var idLeague = 0L
    private val adapter by lazy {
        SimpleListAdapter<TeamModel>(R.layout.item_ranking) { itemView, item, position ->
            val tvPos = itemView.findViewById<CustomTextView>(R.id.tvPos)
            val tvName = itemView.findViewById<CustomTextView>(R.id.tvName)
            val tvDD = itemView.findViewById<CustomTextView>(R.id.tvDD)
            val tvWins = itemView.findViewById<CustomTextView>(R.id.tvWins)
            val tvLost = itemView.findViewById<CustomTextView>(R.id.tvLost)
            val tvBT = itemView.findViewById<CustomTextView>(R.id.tvBT)
            val tvSBT = itemView.findViewById<CustomTextView>(R.id.tvSBT)
            val tvHS = itemView.findViewById<CustomTextView>(R.id.tvHS)
            val tvPoint = itemView.findViewById<CustomTextView>(R.id.tvPoint)
            tvName.text = item.name
            tvPos.text = (position + 1).toString()
            tvDD.text = item.match.toString()
            tvWins.text = item.win.toString()
            tvLost.text = item.lost.toString()
            tvBT.text = item.bt.toString()
            tvSBT.text = item.sbt.toString()
            tvHS.text = item.hs.toString()
            tvPoint.text = item.points.toString()
        }
    }

    override fun setUpViews() {
        initData()
    }

    private fun initData() {
        idLeague = activity?.intent?.getLongExtra(AppPreferenceKey.ID_LEAGUE_CREATE, 0L) ?: 0L
        viewModel.getTeam(idLeague)

        binding.rcvMyMedication.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTeam(idLeague)
    }

    override fun observeData() {
        observe(viewModel.listTeams) {
            adapter.submitList(it)
        }
    }
}
