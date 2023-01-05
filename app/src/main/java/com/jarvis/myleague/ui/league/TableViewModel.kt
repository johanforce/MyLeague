package com.jarvis.myleague.ui.league

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.myleague.data.entities.TeamModel
import com.jarvis.myleague.data.repository.inteface.TeamRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TableViewModel : ViewModel(), KoinComponent {
    private val teamRepository: TeamRepository by inject()

    val listTeams = MutableLiveData<List<TeamModel>>()
    val error = MutableLiveData<String>()

    fun getTeam(idLeague: Long) = viewModelScope.launch {
        try {
            listTeams.value = teamRepository.getTeams(idLeague).sortedByDescending {
                (it.points?:0)*10000000 + (it.hs?:0)*1000000 + (it.bt?:0)*100000 + (it.win?:0)*10000
            }
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
