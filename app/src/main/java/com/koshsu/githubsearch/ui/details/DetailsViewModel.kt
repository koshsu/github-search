package com.koshsu.githubsearch.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koshsu.githubsearch.data.Status
import com.koshsu.githubsearch.data.database.model.Repo
import com.koshsu.githubsearch.data.repository.DetailsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: DetailsRepository
) : ViewModel() {

    private val _repo = MutableLiveData<Status<Repo>>()
    val repo: LiveData<Status<Repo>> get() = _repo

    val isWatchersVisible = MutableLiveData<Boolean>()

    fun getRepo(repoId: Long, ownerLogin: String, repoName: String) {
        viewModelScope.launch {
            repository.getRepoById(repoId, ownerLogin, repoName).collect {
                _repo.value = it
            }
        }
    }

}