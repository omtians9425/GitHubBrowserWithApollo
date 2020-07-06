package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

@ExperimentalCoroutinesApi
fun <T> ApolloCall<T>.toLceFlow() = toFlow().map { response ->
    response.data?.let {
        Lce.Content<T>(it)
    } ?: run {
        // GraphQL Error
        val msg = checkNotNull(response.errors?.joinToString()) {
            "error should not be null when data is null."
        }
        Timber.e(msg)
        Lce.Error<T>(RuntimeException(msg))
    }
}.onStart {
    emit(Lce.Loading<T>())
}.catch {
    // Other Errors. eg: Network connection
    Timber.e("$it")
    emit(Lce.Error(it))
}

// non-extension version (easy to test)
fun <T> toLceFlow(flow: Flow<Response<T>>) = flow.map { response ->
    response.data?.let {
        Lce.Content<T>(it)
    } ?: run {
        val msg = response.errors?.joinToString() ?: "error"
        Timber.e(msg)
        Lce.Error<T>(RuntimeException(msg))
    }
}.onStart {
    emit(Lce.Loading<T>())
}
