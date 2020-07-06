package com.example.githubbrowserwithapollo.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githubbrowserwithapollo.MainCoroutineRule
import com.example.githubbrowserwithapollo.MyReposQuery
import com.example.githubbrowserwithapollo.data.GitHubRepoRepository
import com.example.githubbrowserwithapollo.data.Lce
import com.example.githubbrowserwithapollo.main.MainViewModel.Companion.REPOS_GET_NUM
import com.example.githubbrowserwithapollo.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: GitHubRepoRepository = mockk()
    private val data: MyReposQuery.Data = mockk(relaxed = true)

    @Test
    fun `check const values`() {
        assertThat(REPOS_GET_NUM).isEqualTo(10)
    }

    @Test
    fun `fetchMyRepositories returns ViewState when Content`() = coroutineRule.runBlockingTest {
        every {
            repository.fetchMyRepositories(REPOS_GET_NUM)
        } returns flowOf(Lce.Content(data))

        val viewModel = MainViewModel(repository)
        viewModel.fetchMyRepositories()

        val state = viewModel.viewState.getOrAwaitValue()
        assertThat(state).isEqualTo(MainScreen.ViewState(repoData = data))
        verify(exactly = 1) { repository.fetchMyRepositories(REPOS_GET_NUM) }
    }

    @Test
    fun `fetchMyRepositories returns ErrorSnackbarEffect when Error`() =
        coroutineRule.runBlockingTest {

            every {
                repository.fetchMyRepositories(REPOS_GET_NUM)
            } returns flowOf(Lce.Error(Exception()))

            val viewModel = MainViewModel(repository)
            viewModel.fetchMyRepositories()

            val effect = viewModel.viewEffect.getOrAwaitValue()?.getContentIfNotHandled()
            assertThat(effect).isInstanceOf(MainScreen.ViewEffect.ErrorSnackbarEffect::class.java)
            verify(exactly = 1) { repository.fetchMyRepositories(REPOS_GET_NUM) }
        }
}