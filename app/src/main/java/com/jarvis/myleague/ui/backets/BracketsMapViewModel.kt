package com.jarvis.myleague.ui.backets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.data.repository.inteface.LeagueRepository
import com.jarvis.myleague.data.repository.inteface.MatchesRepository
import com.jarvis.myleague.data.repository.inteface.TeamRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BracketsMapViewModel : ViewModel(), KoinComponent{
    private val matchesRepository: MatchesRepository by inject()
    private val leagueRepository: LeagueRepository by inject()
    private val teamRepository: TeamRepository by inject()

    var listTeams = listOf<TeamModel>()
    var listMatches = listOf<Matches>()
    private var leagueCreate = LeagueModel()

    val isLoadDataSuccess = MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()

    fun getData(idLeague: Long) = viewModelScope.launch {
        try {
            listTeams = teamRepository.getTeams(idLeague)
            listMatches = matchesRepository.getMatches(idLeague)
            leagueCreate = leagueRepository.getLeagueToName(idLeague)
            isLoadDataSuccess.value = true
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
