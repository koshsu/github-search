package com.koshsu.githubsearch.application

import android.app.Application
import com.koshsu.githubsearch.data.database.AppDatabase
import com.koshsu.githubsearch.data.network.GithubApi
import com.koshsu.githubsearch.data.preference.PreferenceProvider
import com.koshsu.githubsearch.data.repository.DetailsRepository
import com.koshsu.githubsearch.data.repository.SearchRepository
import com.koshsu.githubsearch.ui.details.DetailsViewModelFactory
import com.koshsu.githubsearch.ui.search.SearchViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))

        bind() from singleton { GithubApi() }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { SearchRepository(instance(), instance(), instance(), instance()) }
        bind() from singleton { DetailsRepository(instance(), instance(), instance()) }
        bind() from provider { SearchViewModelFactory(instance()) }
        bind() from provider { DetailsViewModelFactory(instance()) }
    }

}