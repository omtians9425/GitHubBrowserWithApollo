package com.example.githubbrowserwithapollo.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Gets the value of a LiveData safely.
 */
@Throws(InterruptedException::class)
fun <T> LiveData<T>.getOrAwaitValue(timeout: Long = 2): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            removeObserver(this)
        }
    }
    observeForever(observer)
    latch.await(timeout, TimeUnit.SECONDS)

    return data
}