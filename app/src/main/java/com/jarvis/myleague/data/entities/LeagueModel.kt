package com.jarvis.myleague.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "league")
data class LeagueModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "max_rounds")
    val maxRounds: Int = 0,
    @ColumnInfo(name = "point_win")
    val pointWin: Int = 0,
    @ColumnInfo(name = "point_draw")
    val pointDraw: Int = 0,
    @ColumnInfo(name = "point_lose")
    val pointLose: Int = 0,
    @ColumnInfo(name = "turn")
    val turn: Int = 0,
    @ColumnInfo(name = "type")
    var type: Int? = null,
    @ColumnInfo(name = "teams")
    var teams: Int? = null,
)