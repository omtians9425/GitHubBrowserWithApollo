package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toFlow
import com.example.githubbrowserwithapollo.MyReposQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GitHubRepoRepositoryImpl(private val apolloClient: ApolloClient) : GitHubRepoRepository {
    override fun fetchMyRepository(): Flow<MyReposQuery.Data?> {
        return apolloClient.query(MyReposQuery(first = 4)).toFlow().map { it.data }
    }
}