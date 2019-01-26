package com.rahul.hope.listeners

interface JobListener {
    fun onJobRequested(code : Int)
    fun makeCall(number : String)
}