package com.example.githubbrowserwithapollo.data

import com.example.githubbrowserwithapollo.MainCoroutineRule
import org.junit.Rule

class ApiHelpersTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

//    @Test
//    fun toLceFlow() = coroutineRule.runBlockingTest {
//        val response = mockk<Response<MyReposQuery.Data>>(relaxed = true)
//        val data = MyReposQuery.Data(mockk(relaxed = true))
//        every { response.data.hint(MyReposQuery.Data::class) } returns data
//
//        val apolloCall = mockk<ApolloClient>(relaxed = true)
//        every { apolloCall.query(any<MyReposQuery>()).toFlow(). } returns flowOf(response)
//
//        val result = apolloCall.query(MyReposQuery(first = 1)).toLceFlow().collect {
//            assertThat(it).isEqualTo(data)
//        }
//    }
}