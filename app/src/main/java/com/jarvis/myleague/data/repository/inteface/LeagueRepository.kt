package com.jarvis.myleague.data.repository.inteface

import com.jarvis.myleague.data.entities.LeagueModel

interface LeagueRepository {

    suspend fun getLeagues(): List<LeagueModel>

    suspend fun addLeague(leagueModel: LeagueModel)

    suspend fun getLeague(nameLeague: String): List<LeagueModel>

    suspend fun getLeagueToName(idLeague: Long): LeagueModel
}
