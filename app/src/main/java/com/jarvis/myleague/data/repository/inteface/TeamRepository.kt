package com.jarvis.myleague.data.repository.inteface

import com.jarvis.myleague.data.entities.TeamModel

interface TeamRepository {

    suspend fun getTeams(idLeague: Long): List<TeamModel>

    suspend fun addTeams(listTeam: List<TeamModel?>)
}
