package com.example.githubbrowserwithapollo.data

sealed class Lce<T> {
    class Loading<T> : Lce<T>()
    data class Content<T>(val data: T) : Lce<T>()
    data class Error<T>(val throwable: Throwable) : Lce<T>()
}