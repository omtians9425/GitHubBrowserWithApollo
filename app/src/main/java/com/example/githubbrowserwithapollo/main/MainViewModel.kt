package com.example.githubbrowserwithapollo.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubbrowserwithapollo.Event
import com.example.githubbrowserwithapollo.data.GitHubRepoRepository
import com.example.githubbrowserwithapollo.data.Lce
import com.example.githubbrowserwithapollo.main.MainScreen.Error
import com.example.githubbrowserwithapollo.main.MainScreen.ViewEffect
import com.example.githubbrowserwithapollo.main.MainScreen.ViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class MainViewModel(private val gitHubRepoRepository: GitHubRepoRepository) : ViewModel() {

    companion object {
        const val REPOS_GET_NUM = 10
    }

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    private val _viewEffect = MutableLiveData<Event<ViewEffect>>()
    val viewEffect: LiveData<Event<ViewEffect>>
        get() = _viewEffect

    fun fetchMyRepositories() {
        gitHubRepoRepository.fetchMyRepositories(REPOS_GET_NUM).onEach { lce ->
            when (lce) {
                is Lce.Loading -> {
                    _viewState.value = ViewState()
                }
                is Lce.Content -> {
                    _viewState.value = ViewState(repoData = lce.data)
                }
                is Lce.Error -> {
                    Timber.e(lce.throwable)
                    _viewEffect.value = Event(ViewEffect.ErrorSnackbarEffect(Error.ApiError))
                }
            }
        }.launchIn(viewModelScope)
    }
}