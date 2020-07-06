package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
fun <T> ApolloCall<T>.toLceFlow() = toFlow().map { response ->
    response.data?.let {
        Timber.i("content")
        Lce.Content<T>(it)
    } ?: run {
        val msg = response.errors?.joinToString() ?: "error"
        Timber.e(msg)
        Lce.Error<T>(RuntimeException(msg))
    }
}.onStart {
    Timber.i("loading")
    emit(Lce.Loading<T>())
}.catch {
    // handle non-GraphQL error.
    Timber.e("$it")
    emit(Lce.Error<T>(it))
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

/**
 * Handle non-GraphQL error [ApolloCall.Callback.onFailure] by callbackFlow.
 * inspired by [ApolloCall.toFlow]
 * But it's may be redundant.
 */
@ExperimentalCoroutinesApi
fun <T> ApolloCall<T>.toLceFlow2(): Flow<Lce<T>> = callbackFlow {
    val clone = clone()
    offer(Lce.Loading<T>())
    clone.enqueue(
        object : ApolloCall.Callback<T>() {
            override fun onResponse(response: Response<T>) {
                runCatching {
                    response.data?.let {
                        Timber.i("$it")
                        offer(Lce.Content(it))
                    } ?: run {
                        val msg = response.errors?.joinToString() ?: "error"
                        Timber.e(msg)
                        offer(Lce.Error<T>(RuntimeException(msg)))
                    }
                }
            }

            override fun onFailure(e: ApolloException) {
                // called for example: "ApolloNetworkException: Failed to execute http call"
                Timber.e("onFailure: $e")
                launch {
                    send(Lce.Error<T>(e))
                    close()
                }
            }

            override fun onStatusEvent(event: ApolloCall.StatusEvent) {
                if (event == ApolloCall.StatusEvent.COMPLETED) {
                    close()
                }
            }
        }
    )
    awaitClose {
        Timber.i("close")
        clone.cancel()
    }
}