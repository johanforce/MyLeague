package com.jarvis.myleague.ui.core

import com.jarvis.myleague.MainApplication
import com.jarvis.myleague.R
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.entities.TeamModel

object RoundRobin {

    fun updateResultMatch(
        matches: Matches,
        scoreHome: String,
        scoreAway: String,
        teamHome: TeamModel,
        teamAway: TeamModel,
        leagueCreate: LeagueModel
    ): Triple<Matches, TeamModel, TeamModel> {
        val scoreHomeOle = matches.scoreHome
        val scoreAwayOld = matches.scoreAway

        matches.scoreAway = if(scoreAway.isEmpty()) null else scoreAway.toInt()
        matches.scoreHome = if(scoreHome.isEmpty()) null else scoreHome.toInt()

        if (scoreAwayOld != null && scoreHomeOle != null) {

            teamHome.apply {
                val hsOld = scoreHomeOle - scoreAwayOld
                this.match = this.match?.minus(1)
                when {
                    hsOld == 0 -> {
                        this.draw = this.draw?.minus(1)
                        this.points = this.points?.minus(leagueCreate.pointDraw)
                    }
                    hsOld < 0 -> {
                        this.lost = this.lost?.minus(1)
                    }
                    else -> {
                        this.win = this.win?.minus(1)
                        this.points = this.points?.minus(leagueCreate.pointWin)
                    }
                }
                this.hs = this.hs?.minus(hsOld)
                this.bt = this.bt?.minus(scoreHomeOle)
                this.sbt = this.sbt?.minus(scoreAwayOld)
            }

            teamAway.apply {
                val hsOld = scoreAwayOld - scoreHomeOle
                this.match = this.match?.minus(1)
                when {
                    hsOld == 0 -> {
                        this.draw = this.draw?.minus(1)
                        this.points = this.points?.minus(leagueCreate.pointDraw)
                    }
                    hsOld < 0 -> {
                        this.lost = this.lost?.minus(1)
                    }
                    else -> {
                        this.win = this.win?.minus(1)
                        this.points = this.points?.minus(leagueCreate.pointWin)
                    }
                }
                this.hs = this.hs?.minus(hsOld)
                this.bt = this.bt?.minus(scoreAwayOld)
                this.sbt = this.sbt?.minus(scoreHomeOle)
            }
        }


        if (scoreAway.isNotEmpty() || scoreHome.isNotEmpty()) {
            teamHome.apply {
                val hs = scoreHome.toInt() - scoreAway.toInt()
                this.match = this.match?.plus(1)
                when {
                    hs == 0 -> {
                        this.draw = this.draw?.plus(1)
                        this.points = this.points?.plus(leagueCreate.pointDraw)
                    }
                    hs < 0 -> {
                        this.lost = this.lost?.plus(1)
                    }
                    else -> {
                        this.win = this.win?.plus(1)
                        this.points = this.points?.plus(leagueCreate.pointWin)
                    }
                }
                this.hs = this.hs?.plus(hs)
                this.bt = this.bt?.plus(scoreHome.toInt())
                this.sbt = this.sbt?.plus(scoreAway.toInt())
            }
            teamAway.apply {
                val hs = scoreAway.toInt() - scoreHome.toInt()
                this.match = this.match?.plus(1)
                when {
                    hs == 0 -> {
                        this.draw = this.draw?.plus(1)
                        this.points = this.points?.plus(leagueCreate.pointDraw)
                    }
                    hs < 0 -> {
                        this.lost = this.lost?.plus(1)
                    }
                    else -> {
                        this.win = this.win?.plus(1)
                        this.points = this.points?.plus(leagueCreate.pointWin)
                    }
                }
                this.hs = this.hs?.plus(hs)
                this.bt = this.bt?.plus(scoreAway.toInt())
                this.sbt = this.sbt?.plus(scoreHome.toInt())
            }
        }

        return Triple(matches, teamHome, teamAway)
    }


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

        val round1 = listResult.map { it.copy() }
        val round2 = listResult.map { it.copy() }
        val maxSingle = round2[round2.size - 1].round
        round2.forEach { it.round = it.round?.plus(maxSingle ?: 0) }

        val listDouble = mutableListOf<Matches>()
        listDouble.addAll(round1)
        listDouble.addAll(round2)
        return if (roundRobin == TurnEnum.TURN_1.value) listResult.toMutableList() else listDouble
    }

    val listShieldLogo = listOf(
        R.drawable.ic_angle_black,
        R.drawable.ic_angle_black_thin,
        R.drawable.ic_angle_blue,
        R.drawable.ic_angle_blue_thin,
        R.drawable.ic_angle_green,
        R.drawable.ic_angle_green_thin,
        R.drawable.ic_angle_pink,
        R.drawable.ic_angle_red,
        R.drawable.ic_angle_red_thin,
        R.drawable.ic_angle_violet,
        R.drawable.ic_angle_yellow,

        R.drawable.ic_rosa_black,
        R.drawable.ic_rosa_black_thin,
        R.drawable.ic_rosa_blue,
        R.drawable.ic_rosa_black_thin,
        R.drawable.ic_rosa_red,
        R.drawable.ic_rosa_red_thin,
        R.drawable.ic_rosa_yellow,

        R.drawable.ic_security_black,
        R.drawable.ic_security_black_thin,
        R.drawable.ic_security_blue,
        R.drawable.ic_security_blue_thin,
        R.drawable.ic_security_red,
        R.drawable.ic_security_red_thin,
        R.drawable.ic_security_yellow,

        R.drawable.ic_shield_black,
        R.drawable.ic_shield_black_thin,
        R.drawable.ic_shield_blue,
        R.drawable.ic_shield_blue_thin,
        R.drawable.ic_shield_red,
        R.drawable.ic_shield_red_thin,
        R.drawable.ic_shield_yellow,
    ) + FlagKit.getListFlagResourceId(MainApplication.applicationContext())
}

enum class StatusMatches(var valueStr: String, val value: Int) {
    // no type
    WIN("WIN", 3),
    DRAW("DRAW", 1),
    LOSE("LOSE", 2),
    NULL("NULL", 0)
}

enum class TurnEnum(var valueStr: String, val value: Int) {
    TURN_1("TURN_1", 0),
    TURN_2("TURN_2", 1),
}
