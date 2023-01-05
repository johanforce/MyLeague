package com.jarvis.myleague.data.repository.repoImpl

import com.jarvis.myleague.data.database.AppDatabase
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.data.repository.inteface.LeagueRepository

class LeagueRepositoryImpl(
    private val database: AppDatabase
) : LeagueRepository {

    override suspend fun getLeagues(): List<LeagueModel> {
        return database.leagueDao().getLeague()
    }

    override suspend fun addLeague(leagueModel: LeagueModel) {
        database.leagueDao().insertLeague(leagueModel)
    }

    override suspend fun getLeague(nameLeague: String):List<LeagueModel> {
        return database.leagueDao().getLeagueToName(nameLeague)
    }

    override suspend fun getLeagueToName(idLeague: Long): LeagueModel {
        return database.leagueDao().getLeagueToName(idLeague)
    }
}
