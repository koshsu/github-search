package com.koshsu.githubsearch.ui.interfaces

/** Allows to do search from activity's SearchView */
interface ISearchListenerFragment {
    fun doSearch(searchQuery: String)
}