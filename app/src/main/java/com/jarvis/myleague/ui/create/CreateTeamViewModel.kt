package com.jarvis.myleague.ui.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.data.repository.inteface.LeagueRepository
import com.jarvis.myleague.data.repository.inteface.MatchesRepository
import com.jarvis.myleague.data.repository.inteface.TeamRepository
import com.jarvis.myleague.ui.core.RoundRobin
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateTeamViewModel : ViewModel(), KoinComponent {
    private val leagueRepository: LeagueRepository by inject()
    private val matchesRepository: MatchesRepository by inject()
    private val teamRepository: TeamRepository by inject()

    val listTeam = mutableListOf<String>()

    var leagueCreate = LeagueModel()

    val isCreateSuccess = MutableLiveData<Boolean>()
    val isCreateMatches = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun getLeagueToId(idLeague: Long) = viewModelScope.launch {
        leagueCreate = leagueRepository.getLeagueToName(idLeague)
    }

    fun createData() = viewModelScope.launch {
        val listTeam = listTeam.map {
            TeamModel(idLeague = leagueCreate.id, name = it)
        }

        try {
            teamRepository.addTeams(listTeam)
            isCreateSuccess.value = true
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun createMatches() = viewModelScope.launch {
        try {
            val listTeam = teamRepository.getTeams(leagueCreate.id)
            val listMatches = RoundRobin.scheduler(listTeam, leagueCreate.id, leagueCreate.turn)
            matchesRepository.addMatches(listMatches)
            isCreateMatches.value = true
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
