package com.example.acyncsampleapp

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import java.lang.Thread.sleep
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Threadで非同期処理
        // スレッドごとにメモリを確保する
        var thread = SampleThread()
        thread.start()

        // Runnableで非同期処理（100スレッド実行してみる）
        // Threadの継承元であるRunnableのインスタンスはメモリ上に1つだけ。
        for (i in 1..100) {
            var runnable = SampleRunnable(i)
            Thread(runnable).start()
        }

        // つまり、Runnableインスタンス１個でこれも可能。
        // スレッドそのものと、スレッドで実行したい処理を分けることができる。
        var runnable2 = SampleRunnable(0)
        for (i in 1..10) {
            Thread(runnable2).start()
        }

        // Threadのインスタンス（メモリ）を使い回す仕組み　→ThreadPool
        // 3個のスレッドインスタンスをメモリに確保する
        var pool:ExecutorService = Executors.newFixedThreadPool(3)
        // 必要に応じてスレッドを新規作成し、一定期間スレッドを使い回すもの
        var pool2:ExecutorService = Executors.newCachedThreadPool()

        for (i in 0..10) {
            // 3個のスレッドプールに10個のタスクを投げる
            Log.d("TESTEST", "====== ThreadPool test Number: ${i} ======")
            pool.submit(runnable2)
        }
        // シャットダウンすることで、新しいタスクをサブミットできなくさせる
        // プログラム終了後もスレッドプールがタスクを待ち続けるのを防ぐ
        pool.shutdown()

        // Handlerは、スレッド間の通信をするために使う。
        // 厳密にはLooperが動作しているスレッド。
        // 別スレッドでHandlerを使って、Messageを投げる
        // それをメインスレッド上でHandlerからMessageを受け取れば、別スレッドの実行結果を受け取り、UIへ反映することが可能。

        /* AsyncTask */
        // この仕組みを簡単にし、スレッドプール と合わせて提供しているクラスがAsyncTask。
        // AndroidのOSごとにスレッドが並列実行か順次実行か違いがあるが、概ね同じ。
        // doInBackground()　-> 非同期で実行された際に裏側で動く処理を記載
        // onPostExecute() -> doInBackground()の結果をHandler経由で受け取ってメインスレッドで実行する
        var sampleAsyncTask = SampleAsyncTask()
        sampleAsyncTask.execute()

        /* Service */
        // 画面を持たないコンポーネント（Activityとかと同じ仲間）
        // 一言で言えば、バックグラウンドで処理する物のこと
        // 音楽アプリを作る場合、他のアクティビティに切り替えた際、音楽が停止してしまう。
        // Activityのライフサイクルとは切り離して別で、処理を実行するための仕組み。
        // Bindを使えば、Activityからの制御も可能。
//        startService(Intent(this, SampleService().class)) Oreoからは書き方が異なる↓
//        startForegroundService(Intent(this, SampleService::class.java))
//        sleep(800)
//        stopService(Intent(this, SampleService::class.java))

        /* IntentService */
        // Handlerを使った非同期処理のもう１つの仕組み
        // ServiceというUIを保持しないコンポーネントに対してIntentを渡すと、非同期処理が裏で実行され、Activityと切り離されたところで実行される。
        // 画面を閉じても裏で処理し続けて欲しい際（音楽アプリなど）に使える
        // 非同期のリクエストを、Handlerで別スレッド（HandlerThread）にて実行するため、Intentを投げた順番に1つずつ実行される
        // 全てのタスクが終了すると、通知してくれる
    }
}
