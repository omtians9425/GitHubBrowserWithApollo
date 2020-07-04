package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toFlow
import com.example.githubbrowserwithapollo.MyReposQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GitHubRepoRepositoryImpl(private val apolloClient: ApolloClient) : GitHubRepoRepository {
    override fun fetchMyRepository(first: Int): Flow<Lce<MyReposQuery.Data>> {
        return apolloClient.query(MyReposQuery(first))
            .toFlow()
            .onStart { Lce.Loading<MyReposQuery.Data>() }
            .map { response ->
                response.data?.let { Lce.Content(it) }
                    ?: run { Lce.Error<MyReposQuery.Data>(response.errors!!.joinToString()) }
            }
    }
}