package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.ApolloClient
import com.example.githubbrowserwithapollo.MainCoroutineRule
import com.example.githubbrowserwithapollo.MyReposQuery
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GitHubRepoRepositoryImplTest {

    @get: Rule
    val coroutineRule = MainCoroutineRule()

    private val apolloClient = mockk<ApolloClient>(relaxed = true)

    @Test
    fun fetchMyRepositories() = coroutineRule.runBlockingTest {
        val repo = GitHubRepoRepositoryImpl(apolloClient)

        repo.fetchMyRepositories(first = 1).toList()

        verify(exactly = 1) { apolloClient.query(eq(MyReposQuery(1))) }
    }

    @Test
    fun fetchMyRepositories2() = coroutineRule.runBlockingTest {
        val repo = GitHubRepoRepositoryImpl(apolloClient)

        val result = repo.fetchMyRepositories(first = 1).toList()

        assertThat(result.size).isEqualTo(2)
        assertThat(result[0]).isInstanceOf(Lce.Loading::class.java)
        assertThat(result[1]).isInstanceOf(Lce.Content::class.java) //fails. we need to mock toLce()
        verify(exactly = 1) { apolloClient.query(any<MyReposQuery>()) }
    }
}