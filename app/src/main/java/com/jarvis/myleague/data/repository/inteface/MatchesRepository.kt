package com.jarvis.myleague.data.repository.inteface

import com.jarvis.myleague.data.entities.Matches

interface MatchesRepository {

    suspend fun getMatches(idLeague: Long): List<Matches>

    suspend fun addMatches(listMatches: List<Matches>)

    suspend fun getMatchesToId(idTeam: Long): List<Matches>
}
