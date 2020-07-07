package com.example.githubbrowserwithapollo.di

import com.apollographql.apollo.ApolloClient
import com.example.githubbrowserwithapollo.BuildConfig
import okhttp3.OkHttpClient
import org.koin.dsl.module

val apiModule = module {
    single {
        val okHttpClient = OkHttpClient().newBuilder()
            .authenticator { _, response ->
                response.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.GITHUB_ACCESS_TOKEN}")
                    .build()
            }.build()

        ApolloClient.builder()
            .serverUrl("https://api.github.com/graphql")
            .enableAutoPersistedQueries(true)
            .useHttpGetMethodForPersistedQueries(true)
            .okHttpClient(okHttpClient)
            .build()
    }
}