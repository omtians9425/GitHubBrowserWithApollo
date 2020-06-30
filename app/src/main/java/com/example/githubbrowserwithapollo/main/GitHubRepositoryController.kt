package com.example.githubbrowserwithapollo.main

import com.airbnb.epoxy.TypedEpoxyController
import com.example.githubbrowserwithapollo.MyReposQuery
import com.example.githubbrowserwithapollo.reposListHeader
import com.example.githubbrowserwithapollo.reposListItem
import timber.log.Timber

class GitHubRepositoryController : TypedEpoxyController<MyReposQuery.Data>() {
    override fun buildModels(data: MyReposQuery.Data?) {
        val repos = data?.viewer?.repositories?.nodes ?: return

        reposListHeader {
            id("header")
            text("This is header")
        }

        repos.forEach {
            reposListItem {
                Timber.i("$it")
                it ?: return@forEach
                id(it.id)
                name(it.name)
                repoId(it.id)
                url(it.url as String)
            }
        }
    }
}