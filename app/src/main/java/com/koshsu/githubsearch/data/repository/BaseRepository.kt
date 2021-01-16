package com.koshsu.githubsearch.data.repository

import com.koshsu.githubsearch.data.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.lang.Exception

/**
 * Base repository class
 *
 * [DB] is for database
 * [RESPONSE] is for network
 * [DATA] is for the type of extracted from [RESPONSE]
 * */
abstract class BaseRepository<RESPONSE, DATA, DB> {

    // Flow builder
    fun asFlow() = flow<Status<DB>> {

        // Emit loading status
        emit(Status.loading())

        // If there's network connection try to fetch data from remote api before local db
        if (shouldFetch()) {
            try {
                // Fetch data from remote api
                val response = fetchRemote()

                // Parse data from response
                val data = getDataFromResponse(response)

                // Response validation
                if (response.isSuccessful && data != null) {
                    // Save data to database
                    saveRemote(data)
                } else {
                    // Emit error
                    emit(Status.error(response.message()))
                }
            } catch (e: Exception) {
                emit(Status.error("Network error!"))
                e.printStackTrace()
            }
        }

        // get data from local db
        emitAll(fetchLocal().map {
            Status.success(it)
        })

    }

    // Fetches response
    protected abstract suspend fun fetchRemote(): Response<RESPONSE>

    // Extracts data from remote response (ex.: response.body()!! or response.body()!!.items)
    protected abstract fun getDataFromResponse(response: Response<RESPONSE>): DATA

    // Saves remote data to local db
    protected abstract suspend fun saveRemote(data: DATA)

    // Fetches data from local database
    protected abstract fun fetchLocal(): Flow<DB>

    // Checks network connection
    protected abstract fun shouldFetch(): Boolean

}