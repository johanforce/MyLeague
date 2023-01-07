package com.jarvis.myleague.data.repository.repoImpl

import com.jarvis.myleague.data.database.AppDatabase
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.repository.inteface.MatchesRepository

class MatchesRepositoryImpl(
    private val database: AppDatabase
) : MatchesRepository {

    override suspend fun getMatches(idLeague: Long): List<Matches> {
        return database.matchesDao().getMatches(idLeague)
    }

    override suspend fun addMatches(listMatches: List<Matches>) {
        database.matchesDao().insertMatches(listMatches)
    }

    override suspend fun getMatchesToId(idTeam: Long): List<Matches> {
        return database.matchesDao().getMatchesToIdTeam(idTeam)
    }
}
