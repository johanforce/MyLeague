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
}
