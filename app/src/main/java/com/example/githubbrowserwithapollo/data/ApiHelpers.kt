package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

@ExperimentalCoroutinesApi
fun <T> ApolloCall<T>.toLceFlow() = toFlow().map { response ->
    response.data?.let {
        Lce.Content<T>(it)
    } ?: run {
        val msg = response.errors!!.joinToString()
        Timber.e(msg)
        Lce.Error<T>(msg)
    }
}.onStart {
    emit(Lce.Loading<T>())
}

// non-extension version (easy to test)
fun <T> toLceFlow(flow: Flow<Response<T>>) = flow.map { response ->
    response.data?.let {
        Lce.Content<T>(it)
    } ?: run {
        val msg = response.errors!!.joinToString()
        Timber.e(msg)
        Lce.Error<T>(msg)
    }
}.onStart {
    emit(Lce.Loading<T>())
}