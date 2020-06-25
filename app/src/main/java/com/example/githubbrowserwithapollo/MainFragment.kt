package com.example.githubbrowserwithapollo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val okHttpClient = OkHttpClient().newBuilder()
            .authenticator { _, response ->
                response.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.GITHUB_ACCESS_TOKEN}")
                    .build()
            }.build()

        val apolloClient = ApolloClient.builder()
            .serverUrl("https://api.github.com/graphql")
            .okHttpClient(okHttpClient)
            .build()

        lifecycleScope.launch {
            val flow = apolloClient.query(MyReposQuery(first = 4)).toFlow().map { it.data }
            flow.collect { Timber.i("$it") }
        }
    }
}