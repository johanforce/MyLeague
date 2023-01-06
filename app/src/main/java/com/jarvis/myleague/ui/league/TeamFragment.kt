package com.jarvis.myleague.ui.league

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.design_system.button.JxButton
import com.jarvis.design_system.textview.CustomEditText
import com.jarvis.design_system.textview.CustomTextView
import com.jarvis.myleague.R
import com.jarvis.myleague.common.click
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.databinding.FragmentTeamBinding
import com.jarvis.myleague.ui.base.BaseFragment
import com.jarvis.myleague.ui.base.adapter.SimpleListAdapter
import com.jarvis.myleague.ui.core.RoundRobin
import com.jarvis.myleague.ui.pref.AppPreferenceKey

@SuppressLint("SetTextI18n")
class TeamFragment :
    BaseFragment<FragmentTeamBinding>(FragmentTeamBinding::inflate) {

    private val viewModel: TeamViewModel by viewModels()
    private var idLeague = 0L
    private val adapter by lazy {
        SimpleListAdapter<TeamModel>(R.layout.item_select_detail) { itemView, item, _ ->
            val icLogo = itemView.findViewById<AppCompatImageView>(R.id.icLogo)
            val tvName = itemView.findViewById<CustomTextView>(R.id.tvName)

            tvName.text = item.name
            icLogo.setImageResource(
                if (item.logo == 0) R.drawable.ic_security_black else item.logo ?: 0
            )
        }
    }

    override fun setUpViews() {
        initData()
    }

    private fun initData() {
        idLeague = activity?.intent?.getLongExtra(AppPreferenceKey.ID_LEAGUE_CREATE, 0L) ?: 0L
        viewModel.getTeam(idLeague)

        binding.rcvMyMedication.adapter = adapter

        adapter.onItemClick = { _, item, _ ->
            showLoadLeague(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTeam(idLeague)
    }

    override fun observeData() {
        observe(viewModel.listTeams) {
            adapter.submitList(it)
        }

        observe(viewModel.isUpdateTeam) {
            viewModel.isUpdateTeam.value = null
            viewModel.getTeam(idLeague)
        }
    }

    private fun showLoadLeague(team: TeamModel) {
        var tempImage = team.logo
        val builder = AlertDialog.Builder(activity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_edit_team, null)
        builder.setView(dialogView)

        val btCancel = dialogView?.findViewById(R.id.btCancel) as? JxButton
        val btEdit = dialogView?.findViewById(R.id.btEdit) as? JxButton
        val recyclerView = dialogView?.findViewById(R.id.rcvMyMedication) as? RecyclerView
        val icLogo = dialogView?.findViewById(R.id.icLogo) as? AppCompatImageView
        val etName = dialogView?.findViewById(R.id.etName) as? CustomEditText

        icLogo?.setImageResource(if (team.logo == 0) R.drawable.ic_security_black else team.logo ?: 0)
        etName?.setText(team.name)

        val alertDialog = builder.create()
        alertDialog?.show()
        alertDialog.setCancelable(false)

        val adapterx by lazy {
            SimpleListAdapter<Int>(R.layout.item_logo) { itemView, item, _ ->
                val icIcon = itemView.findViewById<AppCompatImageView>(R.id.icLogo)
                icIcon.setImageResource(item)
            }
        }

        recyclerView?.adapter = adapterx
        adapterx.submitList(RoundRobin.listShieldLogo)

        adapterx.onItemClick = { _, item, _ ->
            icLogo?.setImageResource(item)
            tempImage = item
        }

        btCancel?.click {
            alertDialog.dismiss()
        }

        btEdit?.click {
            alertDialog.dismiss()
            if (etName?.text.isNullOrEmpty()) return@click
            team.name = etName?.text.toString()
            team.logo = tempImage
            viewModel.updateTeam(teamModel = team, idLeague = idLeague)
        }
    }
}
