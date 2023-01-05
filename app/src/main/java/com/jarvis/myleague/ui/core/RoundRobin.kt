package com.jarvis.myleague.ui.core

import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.entities.TeamModel

object RoundRobin {

    fun scheduler(teams: List<TeamModel>, idLeague: Long, roundRobin: Int): MutableList<Matches> {
        val listMatches = mutableListOf<Matches>()
        val numOfTeams = teams.size
        val evenTeams: Array<TeamModel?>
        var k = 0
        if (numOfTeams % 2 == 0) {
            evenTeams = arrayOfNulls(numOfTeams - 1)
            k = 0
            while (k < numOfTeams - 1) {
                evenTeams[k] = teams[k + 1]
                k++
            }
        } else {
            evenTeams = arrayOfNulls(numOfTeams)
            k = 0
            while (k < numOfTeams - 1) {
                evenTeams[k] = teams[k + 1]
                k++
            }
            evenTeams[numOfTeams - 1]?.name = "Bye"
        }
        val teamsSize = evenTeams.size //it is even number
        val total = teamsSize + 1 - 1 // rounds needed to complete tournament
        val halfSize = (teamsSize + 1) / 2
        var count = 0
        for (week in total - 1 downTo 0) {
            ++count
            val teamIdx = week % teamsSize
            if (evenTeams[teamIdx]?.name != "Bye") {
                listMatches.add(
                    Matches(
                        round = count,
                        idLeague = idLeague,
                        teamHome = teams[0].name,
                        idTeamHome = teams[0].idTeam,
                        teamAway = evenTeams[teamIdx]?.name,
                        idTeamAway = evenTeams[teamIdx]?.idTeam,
                    )
                )
            }
            for (i in 1 until halfSize) {
                val firstTeam = (week + i) % teamsSize
                val secondTeam = (week + teamsSize - i) % teamsSize
                if (evenTeams[firstTeam]?.name != "Bye" && evenTeams[secondTeam]?.name != "Bye") {
                    listMatches.add(
                        Matches(
                            round = count,
                            idLeague = idLeague,
                            teamHome = evenTeams[firstTeam]?.name,
                            idTeamHome = evenTeams[firstTeam]?.idTeam,
                            teamAway = evenTeams[secondTeam]?.name,
                            idTeamAway = evenTeams[secondTeam]?.idTeam
                        )
                    )
                }
            }
        }
        val listResult =
            listMatches.filter { it.teamAway?.isNotEmpty() == true && it.teamHome?.isNotEmpty() == true }
        val listDouble = mutableListOf<Matches>()
        listDouble.addAll(listResult)
        listDouble.addAll(listResult)
        return if (roundRobin == TurnEnum.TURN_1.value) listResult.toMutableList() else listDouble
    }
}

enum class StatusMatches(var valueStr: String, val value: Int) {
    // no type
    WIN("WIN", 0),
    DRAW("DRAW", 1),
    LOSE("LOSE", 2)
}

enum class TurnEnum(var valueStr: String, val value: Int) {
    TURN_1("TURN_1", 0),
    TURN_2("TURN_2", 1),
}
