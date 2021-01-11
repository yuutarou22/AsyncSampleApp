package com.example.acyncsampleapp

import android.os.AsyncTask
import android.util.Log

class SampleAsyncTask: AsyncTask<Void, Int, Void>() {

    // doInBackgroundの前に実行される。非同期処理の前に実行したい処理
    override fun onPreExecute() {
        Log.d("TESTEST AsyncTask", "onPreEcecute")
        Thread.sleep(800)
    }

    // 非同期処理
    override fun doInBackground(vararg param: Void?): Void? {
        for (i in 1..10) {
            publishProgress(i)
            Thread.sleep(500)
        }
        return null
    }

    // 非同期処理中のメインスレッドで実行する処理
    // プログレスバーの表示に主に使用する
    override fun onProgressUpdate(vararg values: Int?) {
        Log.d("TESTEST AsyncTask", "values[0].toString(): ${values[0].toString()}")
    }

    // 非同期処理終了後、結果をメインスレッドに返す
    override fun onPostExecute(result: Void?) {
        Log.d("TESTEST AsyncTask", "onPostExecute")
    }
}