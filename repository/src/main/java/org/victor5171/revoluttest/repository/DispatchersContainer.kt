package org.victor5171.revoluttest.repository

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersContainer {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}
