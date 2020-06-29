package com.example.githubbrowserwithapollo.main

import androidx.lifecycle.*
import com.example.githubbrowserwithapollo.MyReposQuery
import com.example.githubbrowserwithapollo.data.GitHubRepoRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(gitHubRepoRepository: GitHubRepoRepository) : ViewModel() {
    private val _myRepos = MutableLiveData<MyReposQuery.Data?>()
    val myRepos: LiveData<MyReposQuery.Data?>
        get() = _myRepos.distinctUntilChanged()

    init {
        gitHubRepoRepository.fetchMyRepository().onEach {
            _myRepos.value = it
        }.launchIn(viewModelScope)
    }
}