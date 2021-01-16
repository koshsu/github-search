package com.koshsu.githubsearch.data.network

import com.koshsu.githubsearch.data.database.model.Repo
import com.koshsu.githubsearch.data.network.response.SearchResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL: String = "https://api.github.com/"
const val GET_REPOS: String = "search/repositories"


interface GithubApi {

    // Search
    @GET(GET_REPOS)
    suspend fun searchRepos(@Query("q") searchQuery: String): Response<SearchResponse>

    @GET(GET_REPOS)
    suspend fun searchRepos(@Query("q") searchQuery: String,
                            @Query("per_page") perPage: Int): Response<SearchResponse>

    @GET(GET_REPOS)
    suspend fun searchRepos(@Query("q") searchQuery: String,
                            @Query("page") pageIndex: Int,
                            @Query("per_page") perPage: Int): Response<SearchResponse>


    // Repository info
    @GET("repos/{owner}/{name}")
    suspend fun getRepo(
        @Path("owner") owner: String,   // owner login
        @Path("name") name: String      // repo name
    ): Response<Repo>


    companion object {
        operator fun invoke(): GithubApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApi::class.java)
        }
    }

}