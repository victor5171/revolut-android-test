package org.victor5171.revoluttest

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.victor5171.revoluttest.repository.DispatchersContainer

@ExperimentalCoroutinesApi
class TestDispatchersContainer(testCoroutineDispatcher: TestCoroutineDispatcher) :
    DispatchersContainer {

    override val io = testCoroutineDispatcher
    override val main = testCoroutineDispatcher
}
