package org.victor5171.revoluttest

import kotlinx.coroutines.Dispatchers
import org.victor5171.revoluttest.repository.DispatchersContainer

object UnconfinedTestDispatchersContainer : DispatchersContainer {
    override val io = Dispatchers.Unconfined
    override val main = Dispatchers.Unconfined
}
