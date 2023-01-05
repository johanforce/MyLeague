package com.jarvis.myleague.ui.main

import android.app.AlertDialog
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.design_system.button.JxButton
import com.jarvis.design_system.textview.CustomEditText
import com.jarvis.design_system.textview.CustomTextView
import com.jarvis.myleague.R
import com.jarvis.myleague.common.click
import com.jarvis.myleague.common.observe
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.databinding.ActivityMainBinding
import com.jarvis.myleague.ui.base.BaseActivity
import com.jarvis.myleague.ui.base.adapter.SimpleListAdapter
import com.jarvis.myleague.ui.create.CreateTeamActivity
import com.jarvis.myleague.ui.league.LeagueActivity
import com.jarvis.myleague.ui.pref.AppPreferenceKey.Companion.ID_LEAGUE_CREATE
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModel()

    override fun setUpViews() {
        super.setUpViews()

        initView()
    }

    private fun initView() {
        binding.tvCreate.click {
            showCreateLeague()
        }

        viewModel.getLeague()

        binding.tvLoad.click {
            showLoadLeague()
        }
    }

    override fun observeData() {
        super.observeData()

        observe(viewModel.idLeagueCreate) {
            val intent = Intent(this, CreateTeamActivity::class.java)
            intent.putExtra(ID_LEAGUE_CREATE, it)
            startActivity(intent)
        }
    }

    private fun showCreateLeague() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_create_league, null)
        builder.setView(dialogView)
        val btCreate = dialogView?.findViewById(R.id.btCreate) as? JxButton
        val etWin = dialogView?.findViewById(R.id.etWin) as? CustomEditText
        val etDraw = dialogView?.findViewById(R.id.etDraw) as? CustomEditText
        val etLose = dialogView?.findViewById(R.id.etLose) as? CustomEditText
        val etTurn = dialogView?.findViewById(R.id.etTurn) as? CustomEditText
        val etName = dialogView?.findViewById(R.id.etName) as? CustomEditText
        val btCancel = dialogView?.findViewById(R.id.btCancel) as? JxButton

        val alertDialog = builder.create()
        alertDialog?.show()
        alertDialog.setCancelable(false)

        btCreate?.click {
            if (etWin?.text.isNullOrEmpty() || etDraw?.text.isNullOrEmpty()
                || etLose?.text.isNullOrEmpty() || etTurn?.text.isNullOrEmpty()
                || etName?.text.isNullOrEmpty()
            ) return@click

            alertDialog.dismiss()
            viewModel.nameLeague = etName?.text.toString()
            viewModel.createLeague(
                LeagueModel(
                    name = etName?.text.toString(),
                    pointWin = etWin?.text.toString().toInt(),
                    pointDraw = etDraw?.text.toString().toInt(),
                    pointLose = etLose?.text.toString().toInt(),
                    turn = etTurn?.text.toString().toInt(),
                )
            )
        }

        btCancel?.click {
            alertDialog.dismiss()
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

        val adapter by lazy {
            SimpleListAdapter<LeagueModel>(R.layout.item_select) { itemView, item, position ->
                val tvName = itemView.findViewById<CustomTextView>(R.id.tvName)
                tvName.text = item.name
            }
        }

        recyclerView?.adapter = adapter
        adapter.submitList(viewModel.listLeague)

        adapter.onItemClick = { itemView, item, _ ->
            val intent = Intent(this, LeagueActivity::class.java)
            intent.putExtra(ID_LEAGUE_CREATE, item.id)
            startActivity(intent)
            alertDialog.dismiss()
        }

        btCancel?.click {
            alertDialog.dismiss()
        }
    }
}