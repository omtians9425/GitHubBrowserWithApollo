package com.example.githubbrowserwithapollo.data

import com.example.githubbrowserwithapollo.MyReposQuery
import kotlinx.coroutines.flow.Flow

interface GitHubRepoRepository {
    fun fetchMyRepository(): Flow<MyReposQuery.Data?>
}