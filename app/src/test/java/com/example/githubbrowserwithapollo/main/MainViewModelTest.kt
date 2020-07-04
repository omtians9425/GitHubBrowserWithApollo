package com.example.githubbrowserwithapollo.main

import com.example.githubbrowserwithapollo.MainCoroutineRule
import com.example.githubbrowserwithapollo.data.GitHubRepoRepositoryImpl
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Test
    fun fetchMyRepository() {
        val repository = GitHubRepoRepositoryImpl(mockk())

        val viewModel = MainViewModel(repository)


    }
}