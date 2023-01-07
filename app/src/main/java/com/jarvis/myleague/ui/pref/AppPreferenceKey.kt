@file:Suppress("unused")

package com.jarvis.myleague.ui.pref


interface AppPreferenceKey {
    companion object {
        const val KEY_DARKMODE = "KEY_DARKMODE"
        const val SCORE = "SCORE"
        const val HIGHSCORE = "HIGHSCORE"
        const val TRANSITION = "Transition"
        const val KEY_IS_CHANGE_LANGUAGE = "key_is_change_language"
        const val KEY_LOCALE_SETTING = "KEY_LOCALE_SETTING"
        const val KEY_TIME_LAST_LOAD_DATA= "KEY_TIME_LAST_LOAD_DATA"
        const val KEY_DATA= "KEY_DATA"
        const val KEY_GRAPH= "KEY_GRAPH"

        const val ID_LEAGUE_CREATE= "ID_LEAGUE_CREATE"
        const val ID_TEAM_CREATE= "ID_TEAM_CREATE"
    }
}
