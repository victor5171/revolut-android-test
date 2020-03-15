package org.victor5171.revoluttest.repository

import kotlinx.coroutines.Dispatchers

object RealDispatchersContainer : DispatchersContainer {
    override val io = Dispatchers.IO
    override val main = Dispatchers.Main
}
