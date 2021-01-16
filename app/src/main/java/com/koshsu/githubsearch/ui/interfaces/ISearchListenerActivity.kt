package com.koshsu.githubsearch.ui.interfaces

/** Allows to manipulate activity's SearchView from child fragments in nav_graph.xml */
interface ISearchListenerActivity {
    // every child fragment should have this in onResume() with argument "false" except SearchFragment
    fun showSearchView(isShown: Boolean)
    // to set initial value into SearchView coz 1st request is made from another SearchView in HomeFragment
    fun setSearchText(searchQuery: String)
    // callback to do a real search in SearchFragment
    fun registerSearchFragment(instance: ISearchListenerFragment)
}