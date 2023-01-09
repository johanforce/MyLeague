package com.jarvis.myleague.ui.league

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.myleague.MainApplication
import com.jarvis.myleague.R
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.data.repository.inteface.LeagueRepository
import com.jarvis.myleague.data.repository.inteface.MatchesRepository
import com.jarvis.myleague.data.repository.inteface.TeamRepository
import com.jarvis.myleague.ui.core.RoundRobin
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FixturesViewModel : ViewModel(), KoinComponent {
    private val matchesRepository: MatchesRepository by inject()
    private val leagueRepository: LeagueRepository by inject()
    private val teamRepository: TeamRepository by inject()

    val listTeams = MutableLiveData<List<TeamModel>>()
    val listMatches = MutableLiveData<List<Matches>>()
    private var leagueCreate = LeagueModel()
    private val error = MutableLiveData<String>()

    fun getTeam(idLeague: Long) = viewModelScope.launch {
        try {
            listTeams.value = teamRepository.getTeams(idLeague)
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun getFixture(idLeague: Long) = viewModelScope.launch {
        try {
            val list = matchesRepository.getMatches(idLeague).toMutableList()
            val result = mutableListOf<Matches>()
            list.forEachIndexed { index, item ->
                if (index == 0 || item.round != list[index - 1].round) {
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.week_data, item.round.toString())
                        )
                    )
                    result.add(item)
                } else
                    result.add(item)
            }
            listMatches.value = result
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun updateDataTeam(matches: Matches, scoreHome: String, scoreAway: String, idLeague: Long) = viewModelScope.launch {
        try {

            val teamHome = listTeams.value?.find { it.idTeam == matches.idTeamHome }
            val teamAway = listTeams.value?.find { it.idTeam == matches.idTeamAway }

            val dataUpdate = RoundRobin.updateResultMatch(
                matches,
                scoreHome,
                scoreAway,
                teamHome?: TeamModel(),
                teamAway?: TeamModel(),
                leagueCreate
            )

            teamRepository.addTeams(listOf(dataUpdate.second, dataUpdate.third))
            matchesRepository.addMatches(listOf(dataUpdate.first))
            getFixture(idLeague)
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun getLeagueToId(idLeague: Long) = viewModelScope.launch {
        leagueCreate = leagueRepository.getLeagueToName(idLeague)
    }

}
