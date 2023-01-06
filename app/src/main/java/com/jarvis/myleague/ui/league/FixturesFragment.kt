package com.jarvis.myleague.ui.league

import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.jarvis.design_system.textview.CustomEditText
import com.jarvis.design_system.textview.CustomTextView
import com.jarvis.myleague.R
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.databinding.FragmentFixturesBinding
import com.jarvis.myleague.ui.base.BaseFragment
import com.jarvis.myleague.ui.base.adapter.SimpleListAdapter
import com.jarvis.myleague.ui.pref.AppPreferenceKey

class FixturesFragment :
    BaseFragment<FragmentFixturesBinding>(FragmentFixturesBinding::inflate) {

    private val viewModel: FixturesViewModel by viewModels()
    var idLeague = 0L

    private val adapter by lazy {
        SimpleListAdapter<Matches>(R.layout.item_match) { itemView, item, _ ->
            val tvHome = itemView.findViewById<CustomTextView>(R.id.tvHome)
            val tvAway = itemView.findViewById<CustomTextView>(R.id.tvAway)
            val etScoreHome = itemView.findViewById<CustomEditText>(R.id.etScoreHome)
            val etScoreAway = itemView.findViewById<CustomEditText>(R.id.etScoreAway)
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
                etScoreHome.setText(if (item.scoreHome == null) "" else item.scoreHome.toString())
                etScoreAway.setText(if (item.scoreAway == null) "" else item.scoreAway.toString())

                etScoreAway.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (etScoreAway.text.isNullOrEmpty() || etScoreHome.text.isNullOrEmpty()) return@setOnEditorActionListener false
                        viewModel.updateDataTeam(
                            item,
                            etScoreHome.text.toString().toInt(),
                            etScoreAway.text.toString().toInt()
                        )
                    }
                    true
                }

                etScoreHome.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (etScoreAway.text.isNullOrEmpty() || etScoreHome.text.isNullOrEmpty()) return@setOnEditorActionListener false
                        viewModel.updateDataTeam(
                            item,
                            etScoreHome.text.toString().toInt(),
                            etScoreAway.text.toString().toInt()
                        )
                    }
                    true
                }
            }
        }
    }

    override fun setUpViews() {
        initData()
    }

    private fun initData() {
        idLeague = activity?.intent?.getLongExtra(AppPreferenceKey.ID_LEAGUE_CREATE, 0L) ?: 0L
        viewModel.getFixture(idLeague)
        viewModel.getTeam(idLeague)
        viewModel.getLeagueToId(idLeague)
        binding.rcvMyMedication.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFixture(idLeague)
    }

    override fun observeData() {
        observe(viewModel.listMatches) {
            adapter.submitList(it)
        }

        observe(viewModel.isUpdateTeam) {
            viewModel.isUpdateTeam.value = null
            viewModel.updateMatches()
        }
    }
}
