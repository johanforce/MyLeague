package com.jarvis.myleague.ui.backets

import android.app.AlertDialog
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.jarvis.design_system.button.JxButton
import com.jarvis.design_system.textview.CustomEditText
import com.jarvis.design_system.textview.CustomTextView
import com.jarvis.myleague.R
import com.jarvis.myleague.common.click
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.databinding.FragmentResultsBinding
import com.jarvis.myleague.ui.base.BaseFragment
import com.jarvis.myleague.ui.base.adapter.SimpleListAdapter
import com.jarvis.myleague.ui.core.Brackets

class ResultsFragment :
    BaseFragment<FragmentResultsBinding>(FragmentResultsBinding::inflate) {

    private val viewModel: ResultViewModel by viewModels()

    private val adapter by lazy {
        SimpleListAdapter<Matches>(R.layout.item_match) { itemView, item, _ ->
            val tvHome = itemView.findViewById<CustomTextView>(R.id.tvHome)
            val tvAway = itemView.findViewById<CustomTextView>(R.id.tvAway)
            val etScoreHome = itemView.findViewById<CustomTextView>(R.id.etScoreHome)
            val etScoreAway = itemView.findViewById<CustomTextView>(R.id.etScoreAway)
            val tvWeek = itemView.findViewById<CustomTextView>(R.id.tvWeek)
            val llData = itemView.findViewById<LinearLayoutCompat>(R.id.llData)

            if (item.id == null) {
                tvWeek.isVisible = true
                tvWeek.text = item.teamHome
                llData.isVisible = false
            } else {
                tvWeek.isVisible = false
                llData.isVisible = true
                etScoreHome.imeOptions = EditorInfo.IME_ACTION_DONE
                etScoreAway.imeOptions = EditorInfo.IME_ACTION_DONE

                tvHome.text = item.teamHome
                tvAway.text = item.teamAway
                etScoreHome.text = if (item.scoreHome == null) "" else item.scoreHome.toString()
                etScoreAway.text = if (item.scoreAway == null) "" else item.scoreAway.toString()
            }
        }
    }

    override fun setUpViews() {
        initData()
    }

    private fun initData() {
        viewModel.getFixture((activity as BracketsActivity).idLeague)
        binding.rcvMyMedication.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFixture((activity as BracketsActivity).idLeague)
    }

    override fun observeData() {
        observe(viewModel.isLoadDataSuccess) {
            viewModel.isLoadDataSuccess.value = null

            adapter.submitList(viewModel.listMatches)

            adapter.onItemClick = { _, item, _ ->
                if (item.idTeamHome == null || item.idTeamAway == null) {
                    //do nothing
                } else {
                    val listResult = viewModel.listMatches.filter { it.id != null }
                    val indexMatch = listResult.indexOfFirst { it.id == item.id }
                    if (!Brackets.isNextRoundData(indexMatch, listResult, viewModel.listTeams.size))
                        showDetailMatch(
                            item,
                            viewModel.listTeams.first { it.idTeam == item.idTeamHome },
                            viewModel.listTeams.first { it.idTeam == item.idTeamAway })
                }
            }
        }
    }

    private fun showDetailMatch(matches: Matches, teamHome: TeamModel, teamAway: TeamModel) {
        if (matches.idTeamAway == null || matches.idTeamHome == null) return
        val builder = AlertDialog.Builder(activity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_update_detail, null)
        builder.setView(dialogView)

        val btUpdate = dialogView?.findViewById(R.id.btUpdate) as? JxButton
        val btCancel = dialogView?.findViewById(R.id.btCancel) as? JxButton
        val icHome = dialogView?.findViewById(R.id.icHome) as? AppCompatImageView
        val icAway = dialogView?.findViewById(R.id.icAway) as? AppCompatImageView
        val tvHome = dialogView?.findViewById(R.id.tvHome) as? CustomTextView
        val tvAway = dialogView?.findViewById(R.id.tvAway) as? CustomTextView
        val etScoreHome = dialogView?.findViewById(R.id.etScoreHome) as? CustomEditText
        val etScoreAway = dialogView?.findViewById(R.id.etScoreAway) as? CustomEditText

        tvHome?.text = teamHome.name
        tvAway?.text = teamAway.name
        icHome?.setImageResource(
            if (teamHome.logo != 0) teamHome.logo ?: 0 else R.drawable.ic_security_black
        )
        icAway?.setImageResource(
            if (teamAway.logo != 0) teamAway.logo ?: 0 else R.drawable.ic_security_black
        )
        etScoreHome?.setText(if (matches.scoreHome == null) "0" else matches.scoreHome.toString())
        etScoreAway?.setText(if (matches.scoreAway == null) "0" else matches.scoreAway.toString())

        val alertDialog = builder.create()
        alertDialog?.show()
        alertDialog.setCancelable(false)

        btUpdate?.click {
            alertDialog.dismiss()
            viewModel.updateDataTeam(
                matches,
                etScoreHome?.text.toString(),
                etScoreAway?.text.toString(),
                (activity as BracketsActivity).idLeague
            )
        }
        btCancel?.click {
            alertDialog.dismiss()
        }
    }
}
