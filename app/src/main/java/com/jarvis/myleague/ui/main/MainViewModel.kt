package com.jarvis.myleague.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.myleague.data.entities.LeagueModel
import com.jarvis.myleague.data.repository.inteface.LeagueRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {
    private val leagueRepository: LeagueRepository by inject()

    val isCreateSuccess = MutableLiveData<Boolean>()
    var listLeague = listOf<LeagueModel>()

    var nameLeague = ""
    val idLeagueCreate = MutableLiveData<Long>()
    val error = MutableLiveData<String>()

    fun createLeague(league: LeagueModel)= viewModelScope.launch {
        try {
            leagueRepository.addLeague(league)
            idLeagueCreate.value = leagueRepository.getLeague(nameLeague).firstOrNull()?.id
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun getLeague() = viewModelScope.launch {
        try {
            listLeague = leagueRepository.getLeagues()
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
