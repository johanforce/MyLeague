package com.jarvis.myleague.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team")
data class TeamModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_team")
    val idTeam: Long = 0,
    @ColumnInfo(name = "id_league")
    val idLeague: Long = 0,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "match")
    var match: Int? = 0,
    @ColumnInfo(name = "win")
    var win: Int? = 0,
    @ColumnInfo(name = "draw")
    var draw: Int? = 0,
    @ColumnInfo(name = "lost")
    var lost: Int? = 0,
    @ColumnInfo(name = "bt")
    var bt: Int? = 0,
    @ColumnInfo(name = "sbt")
    var sbt: Int? = 0,
    @ColumnInfo(name = "hs")
    var hs: Int? = 0,
    @ColumnInfo(name = "points")
    var points: Int? = 0,
    @ColumnInfo(name = "logo")
    var logo: Int? = 0,
)