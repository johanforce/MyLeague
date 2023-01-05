package com.jarvis.myleague.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class Matches(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_matches")
    var id: Int? = null,
    @ColumnInfo(name = "id_league")
    var idLeague: Long? = null,
    @ColumnInfo(name = "round")
    var round: Int? = null,
    @ColumnInfo(name = "team_home")
    var teamHome: String? = null,
    @ColumnInfo(name = "id_team_home")
    var idTeamHome: Long? = null,
    @ColumnInfo(name = "score_home")
    var scoreHome: Int? = null,
    @ColumnInfo(name = "team_away")
    var teamAway: String? = null,
    @ColumnInfo(name = "id_team_away")
    var idTeamAway: Long? = null,
    @ColumnInfo(name = "score_away")
    var scoreAway: Int? = null,
)