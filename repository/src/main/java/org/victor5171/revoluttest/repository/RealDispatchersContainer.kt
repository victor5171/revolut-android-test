package org.victor5171.revoluttest.repository

import kotlinx.coroutines.Dispatchers

internal object RealDispatchersContainer : DispatchersContainer {
    override val io = Dispatchers.IO
    override val main = Dispatchers.Main
}
