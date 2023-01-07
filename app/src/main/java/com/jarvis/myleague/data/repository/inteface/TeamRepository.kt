package com.jarvis.myleague.data.repository.inteface

import com.jarvis.myleague.data.entities.TeamModel

interface TeamRepository {

    suspend fun getTeams(idLeague: Long): List<TeamModel>

    suspend fun getTeamsToIdTeam(idTeam: Long): TeamModel

    suspend fun addTeams(listTeam: List<TeamModel?>)

    suspend fun deleteLeagueToID(idLeague: Long)
}
