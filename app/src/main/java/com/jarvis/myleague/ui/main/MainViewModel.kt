package com.jarvis.myleague.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.data.repository.inteface.LeagueRepository
import com.jarvis.myleague.data.repository.inteface.MatchesRepository
import com.jarvis.myleague.data.repository.inteface.TeamRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {
    private val leagueRepository: LeagueRepository by inject()
    private val teamRepository: TeamRepository by inject()
    private val matchesRepository: MatchesRepository by inject()

    val isCreateSuccess = MutableLiveData<Boolean>()
    var listLeague = MutableLiveData<List<LeagueModel>>()
    var listTeams = MutableLiveData<List<TeamModel>>()

    var nameLeague = ""
    val idLeagueCreate = MutableLiveData<Long>()
    var idLeagueLoadData = 0L
    val error = MutableLiveData<String>()

    fun createLeague(league: LeagueModel) = viewModelScope.launch {
        try {
            leagueRepository.addLeague(league)
            idLeagueCreate.value = leagueRepository.getLeague(nameLeague).firstOrNull()?.id
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun getLeague() = viewModelScope.launch {
        try {
            listLeague.value = leagueRepository.getLeagues()
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

    fun deleteLeagueWithId(idLeague: Long) = viewModelScope.launch {
        try {
            teamRepository.deleteLeagueToID(idLeague)
            leagueRepository.deleteLeagueToID(idLeague)
            matchesRepository.deleteLeagueToID(idLeague)
            getLeague()
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
