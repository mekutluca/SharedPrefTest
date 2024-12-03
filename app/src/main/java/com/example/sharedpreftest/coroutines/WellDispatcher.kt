package com.example.sharedpreftest.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface WellDispatcher {
    fun default(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
}

class WellDispatcherImpl : WellDispatcher {

    override fun default(): CoroutineDispatcher = Dispatchers.Default

    override fun main(): CoroutineDispatcher = Dispatchers.Main

    override fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun io(): CoroutineDispatcher = Dispatchers.IO
}
