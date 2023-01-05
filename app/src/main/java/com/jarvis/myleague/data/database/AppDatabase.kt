package com.jarvis.myleague.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.data.entities.Matches
import com.jarvis.myleague.data.entities.TeamModel

@Database(entities = [LeagueModel::class, Matches::class, TeamModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun leagueDao(): LeagueDao
    abstract fun teamDao(): TeamDao
    abstract fun matchesDao(): MatchesDao

    companion object {
        private const val DATABASE_NAME = "league_app.db"

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
