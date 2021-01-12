package com.example.acyncsampleapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*
import kotlin.concurrent.schedule

class SampleService: Service() {

    lateinit var mTimer: Timer

    // Serviceが生成された時のみ呼び出される
    // 複数回startServiceを実行した場合、初回のみ
    override fun onCreate() {
        Log.d("TESTEST SampleService", "onCreate")
    }

    // startServiceで送られてきたIntentを受け取る
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TESTEST SampleService", "onStartCommand")
        // 1秒毎にループ
        mTimer = Timer(true)
        mTimer?.schedule(1000,1000) {
            Log.d("TESTEST SampleService", "onStartCommand TimerTask Handler post")
        }
        // 強制終了時、システムによる再起動を求める場合は『START_STICKY』を利用
        // 再起動が不要な場合は『START_NOT_STICKY』を利用する
        return START_NOT_STICKY;
    }

    // stopServiceメソッドでServiceが停止したときに呼ばれる
    // ActivityでsotpServiceを呼ばず、アプリキルした場合は「バッググラウンドで処理を続ける」
    override fun onDestroy() {
        Log.d("TESTEST SampleService", "onDestroy")
        if (mTimer != null) {
            mTimer.cancel()
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}