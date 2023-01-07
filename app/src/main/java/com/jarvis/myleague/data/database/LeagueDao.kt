package com.jarvis.myleague.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jarvis.myleague.data.entities.LeagueModel

@Dao
interface LeagueDao {

    @Query("SELECT * FROM league")
    suspend fun getLeague(): List<LeagueModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(leagueModel: LeagueModel)

    @Query("SELECT * FROM league WHERE name = :name")
    suspend fun getLeagueToName(name: String): List<LeagueModel>

    @Query("SELECT * FROM league WHERE id = :idLeague")
    suspend fun getLeagueToName(idLeague: Long): LeagueModel

    @Query("DELETE FROM league where id = :idLeague")
    suspend fun deleteLeagueToID(idLeague: Long)
}
