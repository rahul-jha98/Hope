package com.rahul.hope

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors private constructor(private var diskIO: Executor,
                                       private var networkIO: Executor,
                                       private var mainThread: Executor) {

    companion object {
        private val LOCK = Object()
        private var sInstance : AppExecutors? = null

        fun getInstance() : AppExecutors {
            if(sInstance == null) {
                synchronized(LOCK) {
                    sInstance = AppExecutors(Executors.newSingleThreadExecutor(),
                            Executors.newFixedThreadPool(5),
                            MainThreadExecutor())
                }
            }
            return sInstance!!
        }

        private class MainThreadExecutor : Executor {
            private val mainThreadHandler : Handler = object : Handler(Looper.getMainLooper()){}

            override fun execute(command: Runnable) {
                mainThreadHandler.post(command)
            }
        }
    }


    fun diskIO() : Executor = diskIO
    fun mainThread() : Executor = mainThread
    fun networkIO() : Executor = networkIO
}