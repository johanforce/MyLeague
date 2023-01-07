package com.jarvis.myleague.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jarvis.myleague.data.entities.TeamModel

@Dao
interface TeamDao {

    @Query("SELECT * FROM team WHERE id_league = :idLeague")
    suspend fun getTeams(idLeague: Long): List<TeamModel>

    @Query("SELECT * FROM team WHERE id_team = :idTeam")
    suspend fun getTeamToId(idTeam: Long): TeamModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teamModel: List<TeamModel?>)
}
