package com.koshsu.githubsearch.data.repository

import android.content.Context
import com.koshsu.githubsearch.application.App
import com.koshsu.githubsearch.data.Status
import com.koshsu.githubsearch.data.database.AppDatabase
import com.koshsu.githubsearch.data.database.model.Repo
import com.koshsu.githubsearch.data.network.GithubApi
import com.koshsu.githubsearch.data.network.response.SearchResponse
import com.koshsu.githubsearch.data.preference.PreferenceProvider
import com.koshsu.githubsearch.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class SearchRepository (
    private val api: GithubApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider,
    context: Context
) {

    private val appContext = context.applicationContext

    fun getRepos(searchQuery: String): Flow<Status<List<Repo>>> {

        return object : BaseRepository<SearchResponse, List<Repo>, List<Repo>>() {

            // make request
            override suspend fun fetchRemote(): Response<SearchResponse> = api.searchRepos(searchQuery, 50)

            // extract data
            override fun getDataFromResponse(response: Response<SearchResponse>): List<Repo> = response.body()!!.items

            // save data
            override suspend fun saveRemote(data: List<Repo>) {
                prefs.setSearchQuery(searchQuery)
                db.getRepoAndOwnerDao().insertReposWithOwner(data)
            }

            // return saved data
            override fun fetchLocal(): Flow<List<Repo>> = db.getRepoAndOwnerDao().getReposWithOwner()

            // should fetch data from remote api or local db
            override fun shouldFetch(): Boolean = NetworkUtils.getNetwork(appContext)

        }.asFlow().flowOn(Dispatchers.IO)

    }

}