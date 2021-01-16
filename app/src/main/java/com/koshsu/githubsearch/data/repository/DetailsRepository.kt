package com.koshsu.githubsearch.data.repository

import android.content.Context
import com.koshsu.githubsearch.data.Status
import com.koshsu.githubsearch.data.database.AppDatabase
import com.koshsu.githubsearch.data.database.model.Repo
import com.koshsu.githubsearch.data.network.GithubApi
import com.koshsu.githubsearch.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class DetailsRepository(
    private val api: GithubApi,
    private val db: AppDatabase,
    context: Context
) {

    private val appContext = context.applicationContext

    fun getRepoById(repoId: Long, ownerLogin: String, repoName: String): Flow<Status<Repo>> {

        return object : BaseRepository<Repo, Repo, Repo>() {

            // make request
            override suspend fun fetchRemote(): Response<Repo> = api.getRepo(ownerLogin, repoName)

            // extract data
            override fun getDataFromResponse(response: Response<Repo>): Repo = response.body()!!

            // update repo
            override suspend fun saveRemote(data: Repo) = db.getRepoAndOwnerDao().upsertRepo(data)

            // get data from db
            override fun fetchLocal(): Flow<Repo> = db.getRepoAndOwnerDao().getRepoWithOwnerById(repoId)

            // should fetch data from remote api or local db
            override fun shouldFetch(): Boolean = NetworkUtils.getNetwork(appContext)

        }.asFlow().flowOn(Dispatchers.IO)

    }

}