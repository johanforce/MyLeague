package com.jarvis.myleague.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jarvis.myleague.data.entities.Matches

@Dao
interface MatchesDao {

    @Query("SELECT * FROM matches WHERE id_league = :idLeague")
    suspend fun getMatches(idLeague: Long): List<Matches>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(list: List<Matches>)

    @Query("SELECT * FROM matches WHERE id_team_away = :idTeam OR id_team_home = :idTeam")
    suspend fun getMatchesToIdTeam(idTeam: Long): List<Matches>

    @Query("DELETE FROM matches where id_league = :idLeague")
    suspend fun deleteLeagueToID(idLeague: Long)
}
