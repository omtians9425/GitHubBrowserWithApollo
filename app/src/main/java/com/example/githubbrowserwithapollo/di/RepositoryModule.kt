package com.example.githubbrowserwithapollo.di

import com.example.githubbrowserwithapollo.data.GitHubRepoRepository
import com.example.githubbrowserwithapollo.data.GitHubRepoRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<GitHubRepoRepository> { GitHubRepoRepositoryImpl(apolloClient = get()) }
}