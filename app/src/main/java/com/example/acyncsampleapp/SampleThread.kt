package com.example.acyncsampleapp

import android.util.Log

class SampleThread: Thread() {
    override fun run() {
        for (i in 1..5) {
            try {
                // 非同期で実行する処理
                Log.d("TESTEST", "Thread value: ${i}")
                sleep(400)
            } catch (e: InterruptedException) {
                // エラー処理
            }
        }
        Log.d("TESTEST", "Thread Completed!")
    }
}