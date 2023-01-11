package com.jarvis.myleague.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.design_system.button.JxButton
import com.jarvis.design_system.textview.CustomEditText
import com.jarvis.design_system.textview.CustomTextView
import com.jarvis.myleague.BuildConfig
import com.jarvis.myleague.R
import com.jarvis.myleague.common.click
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.databinding.ActivityMainBinding
import com.jarvis.myleague.databinding.DialogCreateLeagueBinding
import com.jarvis.myleague.ui.backets.BracketsActivity
import com.jarvis.myleague.ui.base.BaseActivity
import com.jarvis.myleague.ui.base.adapter.SimpleListAdapter
import com.jarvis.myleague.ui.core.RoundRobin
import com.jarvis.myleague.ui.core.TypeLeagueEnum
import com.jarvis.myleague.ui.create.CreateTeamActivity
import com.jarvis.myleague.ui.league.LeagueActivity
import com.jarvis.myleague.ui.pref.AppPreferenceKey.Companion.ID_LEAGUE_CREATE
import kotlinx.android.synthetic.main.item_task.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModel()

    val adapterLoad by lazy {
        SimpleListAdapter<LeagueModel>(R.layout.item_select) { itemView, item, _ ->
            val tvName = itemView.findViewById<CustomTextView>(R.id.tvName)
            val icDelete = itemView.findViewById(R.id.icDelete) as? AppCompatImageView

            tvName.text = item.name
            icDelete?.click {
                viewModel.deleteLeagueWithId(item.id)
            }
        }
    }


    override fun setUpViews() {
        super.setUpViews()

        initView()
    }

    private fun initView() {
        binding.tvVersion.text = BuildConfig.VERSION_NAME
        binding.tvCreate.click {
            if ((viewModel.listLeague.value?.size ?: 0) >= 3) {
                Toast.makeText(
                    this,
                    getString(R.string.out_of_limit_league),
                    Toast.LENGTH_SHORT
                ).show()
                return@click
            }
            showCreateLeague()
        }

        viewModel.getLeague()

        binding.tvLoad.click {
            showLoadLeague()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getLeague()
    }

    override fun observeData() {
        super.observeData()

        observe(viewModel.isCreateLeagueSuccess) {
            viewModel.isCreateLeagueSuccess.value = null
            if (it) {
                viewModel.getIdLeague()
            }
        }

        observe(viewModel.idLeagueCreate) {
            val intent = Intent(this, CreateTeamActivity::class.java)
            intent.putExtra(ID_LEAGUE_CREATE, it)
            startActivity(intent)
        }

        observe(viewModel.listTeams) {
            if (it.isNullOrEmpty()) {
                val intent = Intent(this, CreateTeamActivity::class.java)
                intent.putExtra(ID_LEAGUE_CREATE, viewModel.idLeagueLoadData)
                startActivity(intent)
            } else {
                if (viewModel.leagueCreate.type == TypeLeagueEnum.LEAGUE.value) {
                    val intent = Intent(this, LeagueActivity::class.java)
                    intent.putExtra(ID_LEAGUE_CREATE, viewModel.idLeagueLoadData)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, BracketsActivity::class.java)
                    intent.putExtra(ID_LEAGUE_CREATE, viewModel.idLeagueLoadData)
                    startActivity(intent)
                }
            }
        }

        observe(viewModel.listLeague) {
            adapterLoad.submitList(it)
        }

        observe(viewModel.error) {
            Toast.makeText(this, "Add thất bại", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCreateLeague() {
        var tempRound = 1
        var tempTeam = 0
        val builder = AlertDialog.Builder(this)
        val binding:DialogCreateLeagueBinding = DialogCreateLeagueBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.etName.requestFocus()

        val alertDialog = builder.create()
        alertDialog?.show()
        alertDialog.setCancelable(false)

        binding.cbLeague.isChecked = true
        binding.clLeague.isVisible = true
        binding.clCup.isVisible = false

        spinnerData(binding.spRound, RoundRobin.listRound) {
           tempRound = it
        }

        spinnerData(binding.spTeam, RoundRobin.listTeams) {
           tempTeam = it
        }

        binding.spRound.setSelection(tempRound)
        binding.spTeam.setSelection(tempTeam)

        binding.cbCup.setOnCheckedChangeListener { _, isChecked ->
            binding.cbLeague.isChecked = !isChecked
            binding.clCup.isVisible = isChecked
            binding.clLeague.isVisible = !isChecked
        }
        binding.cbLeague.setOnCheckedChangeListener { _, isChecked ->
            binding.cbCup.isChecked = !isChecked
            binding.clLeague.isVisible = isChecked
            binding.clCup.isVisible = !isChecked
        }

        binding.btCreate.click {
            if (
                (binding.cbLeague.isChecked && binding.etName.text.isNullOrEmpty()) ||
                (binding.cbCup.isChecked && binding.etNameCup.text.isNullOrEmpty())
            ) return@click

            alertDialog.dismiss()
            viewModel.nameLeague =
                if (binding.cbLeague.isChecked) binding.etName.text.toString() else binding.etNameCup.text.toString()
            viewModel.createLeague(
                LeagueModel(
                    name = if (binding.cbLeague.isChecked) binding.etName.text.toString() else binding.etNameCup.text.toString(),
                    pointWin = 3,
                    pointDraw = 1,
                    pointLose = 0,
                    turn = RoundRobin.listRound[tempRound],
                    type = if (binding.cbLeague.isChecked) TypeLeagueEnum.LEAGUE.value else TypeLeagueEnum.CUP.value,
                    teams = RoundRobin.listTeams[tempTeam]
                )
            )
        }

        binding.btCancel.click {
            alertDialog.dismiss()
            binding.etName.clearFocus()
        }
    }

    private fun spinnerData(spTeams: Spinner, listItem: List<Int>, choiceTeams: (position:Int) -> Unit = {}) {
        val adapter: ArrayAdapter<Int> =
            ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item, listItem)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spTeams.adapter = adapter

        spTeams.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                choiceTeams(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun showLoadLeague() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_load_league, null)
        builder.setView(dialogView)

        val btCancel = dialogView?.findViewById(R.id.btCancel) as? JxButton
        val recyclerView = dialogView?.findViewById(R.id.rcvMyMedication) as? RecyclerView

        val alertDialog = builder.create()
        alertDialog?.show()
        alertDialog.setCancelable(false)

        recyclerView?.adapter = adapterLoad

        adapterLoad.onItemClick = { _, item, _ ->
            viewModel.idLeagueLoadData = item.id
            viewModel.getTeam(item.id)
            alertDialog.dismiss()
        }

        btCancel?.click {
            alertDialog.dismiss()
        }
    }
}
