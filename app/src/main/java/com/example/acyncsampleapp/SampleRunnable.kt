package com.example.acyncsampleapp

import android.util.Log

class SampleRunnable(i: Int): Runnable {

    var i = i

    override fun run() {
        Log.d("TESTEST", "SampleRunnable test ${i}")
    }
}