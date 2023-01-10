package com.jarvis.myleague.ui.core

import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.entities.TeamModel
import com.ventura.bracketslib.model.ColomnData
import com.ventura.bracketslib.model.CompetitorData
import com.ventura.bracketslib.model.MatchData

object Brackets {
    fun showBracketsCup(list: List<Matches>): List<ColomnData> {
        val comData = mutableListOf<CompetitorData>()

        list.map {
            comData.add(
                CompetitorData(
                    it.teamHome,
                    if (it.scoreHome == null) "-" else it.scoreHome.toString()
                )
            )
            comData.add(
                CompetitorData(
                    it.teamAway,
                    if (it.scoreAway == null) "-" else it.scoreAway.toString()
                )
            )
        }

        val matchesData = mutableListOf<MatchData>()
        comData.forEachIndexed { index, competitorData ->
            if (index % 2 == 0) {
                matchesData.add(MatchData(competitorData, comData[index + 1]))
            }
        }

        val columData = mutableListOf<ColomnData>()
        when (list.size) {
            3 -> {
                columData.add(ColomnData(listOf(matchesData[0], matchesData[1])))
                columData.add(ColomnData(listOf(matchesData[2])))
            }
            7 -> {
                columData.add(
                    ColomnData(
                        listOf(
                            matchesData[0],
                            matchesData[1],
                            matchesData[2],
                            matchesData[3]
                        )
                    )
                )
                columData.add(ColomnData(listOf(matchesData[4], matchesData[5])))
                columData.add(ColomnData(listOf(matchesData[6])))
            }
            15 -> {
                columData.add(
                    ColomnData(
                        listOf(
                            matchesData[0],
                            matchesData[1],
                            matchesData[2],
                            matchesData[3],
                            matchesData[4],
                            matchesData[5],
                            matchesData[6],
                            matchesData[7]
                        )
                    )
                )
                columData.add(
                    ColomnData(
                        listOf(
                            matchesData[8],
                            matchesData[9],
                            matchesData[10],
                            matchesData[11]
                        )
                    )
                )
                columData.add(ColomnData(listOf(matchesData[12], matchesData[13])))
                columData.add(ColomnData(listOf(matchesData[14])))
            }
            else -> {
                columData.add(
                    ColomnData(
                        listOf(
                            matchesData[0], matchesData[1], matchesData[2], matchesData[3],
                            matchesData[4], matchesData[5], matchesData[6], matchesData[7],
                            matchesData[8], matchesData[9], matchesData[10], matchesData[11],
                            matchesData[12], matchesData[13], matchesData[14], matchesData[15],
                        )
                    )
                )
                columData.add(
                    ColomnData(
                        listOf(
                            matchesData[16], matchesData[17], matchesData[18], matchesData[19],
                            matchesData[20], matchesData[21], matchesData[22], matchesData[23]
                        )
                    )
                )
                columData.add(
                    ColomnData(
                        listOf(
                            matchesData[24], matchesData[25], matchesData[26], matchesData[27]
                        )
                    )
                )
                columData.add(
                    ColomnData(
                        listOf(
                            matchesData[28], matchesData[29]
                        )
                    )
                )
                columData.add(ColomnData(listOf(matchesData[30])))
            }
        }
        return columData
    }

    fun schedulerTeam(listTeam: List<TeamModel>): List<Matches> {
        val listMatchBase = mutableListOf<Matches>()
        listTeam.forEachIndexed { index, teamModel ->
            if (index % 2 == 0) {
                listMatchBase.add(
                    Matches(
                        idLeague = teamModel.idLeague,
                        round = 1,
                        teamHome = teamModel.name,
                        idTeamHome = teamModel.idTeam,
                        teamAway = listTeam[index + 1].name,
                        idTeamAway = listTeam[index + 1].idTeam
                    )
                )
            }
        }

        when (listTeam.size) {
            4 -> listMatchBase.add(Matches(idLeague = listTeam[0].idLeague))
            8 -> for (i in 1..3) {
                listMatchBase.add(Matches(idLeague = listTeam[0].idLeague))
            }
            16 -> for (i in 1..7) {
                listMatchBase.add(Matches(idLeague = listTeam[0].idLeague))
            }
            else -> for (i in 1..15) {
                listMatchBase.add(Matches(idLeague = listTeam[0].idLeague))
            }
        }
        return listMatchBase
    }

    fun findIndexNextRound(indexMatch: Int, totalTeam: Int): Int {
        return indexMatch / 2 + totalTeam / 2
    }

    fun isNextRoundData(indexMatch: Int, listMatch: List<Matches>, totalTeam: Int): Boolean {
        val indexNextRound = findIndexNextRound(indexMatch, totalTeam)
        if (indexNextRound > listMatch.size - 1) return false
        val matchNextRound = listMatch[indexNextRound]
        if (matchNextRound.scoreHome != null || matchNextRound.scoreAway != null) return true
        return false
    }

    fun getTeamWinInMatch(matches: Matches): Pair<Long, String> {
        return if ((matches.scoreHome ?: 0) >= (matches.scoreAway ?: 0)) {
            Pair(matches.idTeamHome ?: 0L, matches.teamHome ?: "")
        } else {
            Pair(matches.idTeamAway ?: 0L, matches.teamAway ?: "")
        }
    }
}