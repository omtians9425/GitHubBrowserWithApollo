package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.api.Response
import com.example.githubbrowserwithapollo.MyReposQuery
import kotlinx.coroutines.flow.Flow

interface GitHubRepoRepository {
    fun fetchMyRepository(): Flow<Response<MyReposQuery.Data?>>
}