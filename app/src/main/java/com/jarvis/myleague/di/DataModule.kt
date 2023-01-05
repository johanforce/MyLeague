package com.jarvis.myleague.di

import com.jarvis.myleague.data.database.AppDatabase
import com.jarvis.myleague.data.repository.inteface.LeagueRepository
import com.jarvis.myleague.data.repository.inteface.MatchesRepository
import com.jarvis.myleague.data.repository.inteface.TeamRepository
import com.jarvis.myleague.data.repository.repoImpl.LeagueRepositoryImpl
import com.jarvis.myleague.data.repository.repoImpl.MatchesRepositoryImpl
import com.jarvis.myleague.data.repository.repoImpl.TeamRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        AppDatabase.getInstance(androidContext())
    }
    single<LeagueRepository> {
        LeagueRepositoryImpl(get())
    }

    single<MatchesRepository> {
        MatchesRepositoryImpl(get())
    }

    single<TeamRepository> {
        TeamRepositoryImpl(get())
    }
}
