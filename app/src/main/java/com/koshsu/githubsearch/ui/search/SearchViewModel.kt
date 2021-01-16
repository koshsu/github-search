package com.koshsu.githubsearch.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koshsu.githubsearch.data.Status
import com.koshsu.githubsearch.data.database.model.Repo
import com.koshsu.githubsearch.data.repository.SearchRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {

    private val _repos = MutableLiveData<Status<List<Repo>>>()
    val repos: LiveData<Status<List<Repo>>> get() = _repos

    fun getRepos(searchQuery: String) {
        viewModelScope.launch {
            repository.getRepos(searchQuery).collect {
                _repos.value = it
            }
        }
    }

}