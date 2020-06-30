package com.example.githubbrowserwithapollo.main

import androidx.annotation.StringRes
import com.example.githubbrowserwithapollo.MyReposQuery
import com.example.githubbrowserwithapollo.R

object MainScreen {

    data class ViewState(
        val repoData: MyReposQuery.Data? = null
    )

    sealed class ViewEffect {
        class ErrorSnackbar(val error: Error) : ViewEffect()
    }

    sealed class Error(@StringRes val errorResId: Int) {
        object ApiError : Error(R.string.repos_api_error)
    }
}