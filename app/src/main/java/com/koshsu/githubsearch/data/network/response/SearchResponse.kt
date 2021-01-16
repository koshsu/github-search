package com.koshsu.githubsearch.data.network.response

import com.google.gson.annotations.SerializedName
import com.koshsu.githubsearch.data.database.model.Repo

data class SearchResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    val items: List<Repo>
) : BaseResponse