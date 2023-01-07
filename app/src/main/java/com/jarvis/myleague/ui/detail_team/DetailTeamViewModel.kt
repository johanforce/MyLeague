package com.jarvis.myleague.ui.detail_team

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.data.repository.inteface.MatchesRepository
import com.jarvis.myleague.data.repository.inteface.TeamRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DetailTeamViewModel : ViewModel(), KoinComponent {
    private val matchesRepository: MatchesRepository by inject()
    private val teamRepository: TeamRepository by inject()

    var idTeam = 0L

    var listMatches = MutableLiveData<List<Matches>>()
    var teamModel = MutableLiveData<TeamModel>()

    val error = MutableLiveData<String>()

    fun getTeamToId() = viewModelScope.launch {
        try {
            listMatches.value = matchesRepository.getMatchesToId(idTeam)
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun getDetailTeamToId() = viewModelScope.launch {
        try {
            teamModel.value = teamRepository.getTeamsToIdTeam(idTeam)
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
