package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.example.githubbrowserwithapollo.MyReposQuery
import kotlinx.coroutines.flow.Flow

class GitHubRepoRepositoryImpl(private val apolloClient: ApolloClient) : GitHubRepoRepository {
    override fun fetchMyRepository(): Flow<Response<MyReposQuery.Data?>> {
        return apolloClient.query(MyReposQuery(first = 4)).toFlow()
    }
}