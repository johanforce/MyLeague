package com.jarvis.myleague.di

import com.jarvis.myleague.ui.create.CreateTeamViewModel
import com.jarvis.myleague.ui.detail_team.DetailTeamViewModel
import com.jarvis.myleague.ui.league.FixturesViewModel
import com.jarvis.myleague.ui.league.LeagueViewModel
import com.jarvis.myleague.ui.league.TableViewModel
import com.jarvis.myleague.ui.league.TeamViewModel
import com.jarvis.myleague.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CreateTeamViewModel()
    }
    viewModel {
        MainViewModel()
    }
    viewModel {
        LeagueViewModel()
    }

    viewModel {
        TableViewModel()
    }

    viewModel {
        FixturesViewModel()
    }

    viewModel {
        TeamViewModel()
    }

    viewModel {
        DetailTeamViewModel()
    }
}
