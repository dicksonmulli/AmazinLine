package com.amazinline

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */

open class AppExecutors constructor(
    val diskIO: Executor = DiskIOThreadExecutor(),
    val mainThread: Executor = MainThreadExecutor()
) {

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

}

class DiskIOThreadExecutor : Executor {
    private val diskIO = Executors.newSingleThreadExecutor()

    override fun execute(command: Runnable?) {
        diskIO.execute(command)
    }
}
