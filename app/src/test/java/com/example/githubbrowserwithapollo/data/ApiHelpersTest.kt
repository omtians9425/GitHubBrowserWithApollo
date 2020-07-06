package com.example.githubbrowserwithapollo.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.example.githubbrowserwithapollo.MainCoroutineRule
import com.example.githubbrowserwithapollo.MyReposQuery
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class ApiHelpersTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private val response: Response<MyReposQuery.Data> = mockk(relaxed = true)
    private val apolloCall: ApolloClient = mockk(relaxed = true)

    private fun mockApolloCallToFlow() {
        // use mockkStatic with package name and file name in order to mock the extension function.
        mockkStatic("com.apollographql.apollo.coroutines.CoroutinesExtensionsKt")
        every { apolloCall.query(any<MyReposQuery>()).toFlow() } returns flowOf(response)
    }

    @Test
    fun `toLceFlow returns Content when data is non-null`() = coroutineRule.runBlockingTest {
        every { response.data } returns mockk(relaxed = true)

        mockApolloCallToFlow()

        val result = apolloCall.query(MyReposQuery(1)).toLceFlow().toList()
        assertThat(result[0]).isInstanceOf(Lce.Loading::class.java)
        assertThat(result[1]).isInstanceOf(Lce.Content::class.java)
    }

    @Test
    fun `toLceFlow returns Error when data is null`() = coroutineRule.runBlockingTest {
        every { response.data } returns null

        mockApolloCallToFlow()

        val result = apolloCall.query(MyReposQuery(1)).toLceFlow().toList()
        assertThat(result[0]).isInstanceOf(Lce.Loading::class.java)
        assertThat(result[1]).isInstanceOf(Lce.Error::class.java)
    }

    @Test
    fun `toLceFlow returns Error when exception`() = coroutineRule.runBlockingTest {
        every { response.data } throws RuntimeException()

        mockApolloCallToFlow()

        val result = apolloCall.query(MyReposQuery(1)).toLceFlow().toList()
        assertThat(result[0]).isInstanceOf(Lce.Loading::class.java)
        assertThat(result[1]).isInstanceOf(Lce.Error::class.java)
    }

    @Test
    fun `toLceFlow non-ext returns Content when data is non-null`() =
        coroutineRule.runBlockingTest {
            every { response.data } returns mockk()

            val result = toLceFlow(flowOf(response)).toList()
            assertThat(result[0]).isInstanceOf(Lce.Loading::class.java)
            assertThat(result[1]).isInstanceOf(Lce.Content::class.java)
        }

    @Test
    fun `toLceFlow non-ext returns Error when data is null`() = coroutineRule.runBlockingTest {
        every { response.data } returns null

        val result = toLceFlow(flowOf(response)).toList()
        assertThat(result[0]).isInstanceOf(Lce.Loading::class.java)
        assertThat(result[1]).isInstanceOf(Lce.Error::class.java)
    }
}