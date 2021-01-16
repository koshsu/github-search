package com.koshsu.githubsearch.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_SEARCH_QUERY = "key_search_query"

class PreferenceProvider(
    context: Context
) {

    private val applicationContext = context.applicationContext
    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)

    fun setSearchQuery(query: String) {
        preference.edit().putString(
            KEY_SEARCH_QUERY,
            query
        ).apply()
    }

    fun getSearchQuery() : String = preference.getString(KEY_SEARCH_QUERY, "")!!
}