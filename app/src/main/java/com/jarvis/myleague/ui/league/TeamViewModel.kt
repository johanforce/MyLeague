package com.jarvis.myleague.ui.league

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.data.repository.inteface.MatchesRepository
import com.jarvis.myleague.data.repository.inteface.TeamRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TeamViewModel : ViewModel(), KoinComponent {
    private val teamRepository: TeamRepository by inject()
    private val matchesRepository: MatchesRepository by inject()

    val isUpdateTeam = MutableLiveData<Boolean>()
    val listTeams = MutableLiveData<List<TeamModel>>()
    val error = MutableLiveData<String>()

    fun updateTeam(teamModel: TeamModel, idLeague: Long) = viewModelScope.launch {
        try {
            teamRepository.addTeams(listOf(teamModel))
            val listMatches = matchesRepository.getMatches(idLeague)
            val listAway = listMatches.filter { it.idTeamAway == teamModel.idTeam }
            listAway.forEach {
                it.teamAway = teamModel.name
            }
            val listHome = listMatches.filter { it.idTeamHome == teamModel.idTeam }
            listHome.forEach {
                it.teamHome = teamModel.name
            }
            matchesRepository.addMatches(listAway + listHome)
            isUpdateTeam.value = true
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun getTeam(idLeague: Long) = viewModelScope.launch {
        try {
            listTeams.value = teamRepository.getTeams(idLeague)
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
