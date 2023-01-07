package com.jarvis.myleague.data.repository.repoImpl

import com.jarvis.myleague.data.database.AppDatabase
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.data.repository.inteface.TeamRepository

class TeamRepositoryImpl(
    private val database: AppDatabase
) : TeamRepository {

    override suspend fun getTeams(idLeague: Long): List<TeamModel> {
        return database.teamDao().getTeams(idLeague)
    }

    override suspend fun getTeamsToIdTeam(idTeam: Long): TeamModel {
        return database.teamDao().getTeamToId(idTeam)
    }

    override suspend fun addTeams(listTeam: List<TeamModel?>) {
        return database.teamDao().insertTeams(listTeam)
    }
}
