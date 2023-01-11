package com.jarvis.myleague.ui.backets

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
import com.jarvis.myleague.ui.core.Brackets
import com.jarvis.myleague.ui.core.RoundRobin
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ResultViewModel : ViewModel(), KoinComponent {
    private val matchesRepository: MatchesRepository by inject()
    private val teamRepository: TeamRepository by inject()
    private val leagueRepository: LeagueRepository by inject()

    var idTeam = 0L

    var listMatches = listOf<Matches>()
    var listTeams = listOf<TeamModel>()
    var leagueCreate = LeagueModel()

    val isLoadDataSuccess = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun getFixture(idLeague: Long) = viewModelScope.launch {
        try {
            listTeams = teamRepository.getTeams(idLeague)
            leagueCreate = leagueRepository.getLeagueToName(idLeague)
            val list = matchesRepository.getMatches(idLeague).toMutableList()
            val result = mutableListOf<Matches>()
            when (list.size) {
                3 -> {
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_4)
                        )
                    )
                    for (i in 0..1) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.final_)
                        )
                    )
                    result.add(list[2])
                }
                7 -> {
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_8)
                        )
                    )
                    for (i in 0..3) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_4)
                        )
                    )
                    for (i in 4..5) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.final_)
                        )
                    )
                    result.add(list[6])
                }
                15 -> {
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_16)
                        )
                    )
                    for (i in 0..7) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_8)
                        )
                    )
                    for (i in 8..11) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_4)
                        )
                    )
                    for (i in 12..13) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.final_)
                        )
                    )
                    result.add(list[14])
                }
                else -> {
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_32)
                        )
                    )
                    for (i in 0..15) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_16)
                        )
                    )
                    for (i in 16..23) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_8)
                        )
                    )
                    for (i in 24..27) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.round_4)
                        )
                    )
                    for (i in 28..29) result.add(list[i])
                    result.add(
                        Matches(
                            teamHome = MainApplication.applicationContext()
                                .getString(R.string.final_)
                        )
                    )
                    result.add(list[30])
                }

            }
            listMatches = result
            isLoadDataSuccess.value = true
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun updateDataTeam(
        matches: Matches,
        scoreHome: String,
        scoreAway: String,
        idLeague: Long
    ) = viewModelScope.launch {
        try {
            val teamHome = listTeams.find { it.idTeam == matches.idTeamHome }
            val teamAway = listTeams.find { it.idTeam == matches.idTeamAway }

            val dataUpdate = RoundRobin.updateResultMatch(
                matches,
                scoreHome,
                scoreAway,
                teamHome ?: TeamModel(),
                teamAway ?: TeamModel(),
                leagueCreate
            )
            val listResult = listMatches.filter { it.id != null }
            val indexMatch = listResult.indexOfFirst { it.id == matches.id }
            if (indexMatch == listResult.size - 1) {
                teamRepository.addTeams(listOf(dataUpdate.second, dataUpdate.third))
                matchesRepository.addMatches(listOf(dataUpdate.first))
            } else {
                val nextRoundUpdate = Brackets.findIndexNextRound(indexMatch, listTeams.size)
                val nextMatch = listResult[nextRoundUpdate].copy()
                when {
                    dataUpdate.first.scoreAway == null && dataUpdate.first.scoreHome == null -> {
                        nextMatch.apply {
                            idTeamHome = null
                            this.teamHome = null
                            idTeamAway = null
                            this.teamAway = null
                        }
                    }
                    (indexMatch % 2 == 0) -> {
                        nextMatch.apply {
                            idTeamHome = Brackets.getTeamWinInMatch(matches).first
                            this.teamHome = Brackets.getTeamWinInMatch(matches).second
                        }
                    }
                    else -> {
                        nextMatch.apply {
                            idTeamAway = Brackets.getTeamWinInMatch(matches).first
                            this.teamAway = Brackets.getTeamWinInMatch(matches).second
                        }
                    }
                }

                teamRepository.addTeams(listOf(dataUpdate.second, dataUpdate.third))
                matchesRepository.addMatches(listOf(dataUpdate.first, nextMatch))
            }
            getFixture(idLeague)
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
