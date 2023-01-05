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
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FixturesViewModel : ViewModel(), KoinComponent {
    private val matchesRepository: MatchesRepository by inject()
    private val leagueRepository: LeagueRepository by inject()
    private val teamRepository: TeamRepository by inject()

    private val listTeams = MutableLiveData<List<TeamModel>>()
    val listMatches = MutableLiveData<List<Matches>>()
    private var leagueCreate = LeagueModel()
    private val error = MutableLiveData<String>()

    val isUpdateTeam = MutableLiveData<Boolean>()

    private val listTemp = mutableListOf<Triple<Matches, Int, Int>>()

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

    fun updateDataTeam(matches: Matches, scoreHome: Int, scoreAway: Int) = viewModelScope.launch {
        try {
            listTemp.add(Triple(matches, scoreHome, scoreAway))

            val teamHome = listTeams.value?.find { it.idTeam == matches.idTeamHome }
            val teamAway = listTeams.value?.find { it.idTeam == matches.idTeamAway }

            //before
            if (matches.scoreHome != null && matches.scoreAway != null) {
                val scoreHomeOle = matches.scoreHome ?: 0
                val scoreAwayOld = matches.scoreAway ?: 0

                teamHome.apply {
                    val hsOld = scoreHomeOle - scoreAwayOld
                    this?.match = this?.match?.minus(1)
                    when {
                        hsOld == 0 -> {
                            this?.draw = this?.draw?.minus(1)
                            this?.points = this?.points?.minus(leagueCreate.pointDraw)
                        }
                        hsOld < 0 -> {
                            this?.lost = this?.lost?.minus(1)
                        }
                        else -> {
                            this?.win = this?.win?.minus(1)
                            this?.points = this?.points?.minus(leagueCreate.pointWin)
                        }
                    }
                    this?.hs = this?.hs?.minus(hsOld)
                    this?.bt = this?.bt?.minus(scoreHomeOle)
                    this?.sbt = this?.sbt?.minus(scoreAwayOld)
                }

                teamAway.apply {
                    val hsOld = scoreAwayOld - scoreHomeOle
                    this?.match = this?.match?.minus(1)
                    when {
                        hsOld == 0 -> {
                            this?.draw = this?.draw?.minus(1)
                            this?.points = this?.points?.minus(leagueCreate.pointDraw)
                        }
                        hsOld < 0 -> {
                            this?.lost = this?.lost?.minus(1)
                        }
                        else -> {
                            this?.win = this?.win?.minus(1)
                            this?.points = this?.points?.minus(leagueCreate.pointWin)
                        }
                    }
                    this?.hs = this?.hs?.minus(hsOld)
                    this?.bt = this?.bt?.minus(scoreAwayOld)
                    this?.sbt = this?.sbt?.minus(scoreHomeOle)
                }
            }

            teamHome.apply {
                val hs = scoreHome - scoreAway
                this?.match = this?.match?.plus(1)
                when {
                    hs == 0 -> {
                        this?.draw = this?.draw?.plus(1)
                        this?.points = this?.points?.plus(leagueCreate.pointDraw)
                    }
                    hs < 0 -> {
                        this?.lost = this?.lost?.plus(1)
                    }
                    else -> {
                        this?.win = this?.win?.plus(1)
                        this?.points = this?.points?.plus(leagueCreate.pointWin)
                    }
                }
                this?.hs = this?.hs?.plus(hs)
                this?.bt = this?.bt?.plus(scoreHome)
                this?.sbt = this?.sbt?.plus(scoreAway)
            }
            teamAway.apply {
                val hs = scoreAway - scoreHome
                this?.match = this?.match?.plus(1)
                when {
                    hs == 0 -> {
                        this?.draw = this?.draw?.plus(1)
                        this?.points = this?.points?.plus(leagueCreate.pointDraw)
                    }
                    hs < 0 -> {
                        this?.lost = this?.lost?.plus(1)
                    }
                    else -> {
                        this?.win = this?.win?.plus(1)
                        this?.points = this?.points?.plus(leagueCreate.pointWin)
                    }
                }
                this?.hs = this?.hs?.plus(hs)
                this?.bt = this?.bt?.plus(scoreAway)
                this?.sbt = this?.sbt?.plus(scoreHome)
            }

            teamRepository.addTeams(listOf(teamHome, teamAway))
            isUpdateTeam.value = true
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun updateMatches() = viewModelScope.launch {
        try {
            listTemp[0].first.apply {
                this.scoreHome = listTemp[0].second
                this.scoreAway = listTemp[0].third
            }
            matchesRepository.addMatches(listOf(listTemp[0].first))
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun getLeagueToId(idLeague: Long) = viewModelScope.launch {
        leagueCreate = leagueRepository.getLeagueToName(idLeague)
    }

}
