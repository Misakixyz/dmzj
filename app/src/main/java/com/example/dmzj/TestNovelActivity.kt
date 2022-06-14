package com.example.dmzj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_novel.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class TestNovelActivity : AppCompatActivity() {
    var novelStr:String=""
    private fun sendRequestWithHttpURLConnection(){
        thread{
            var connection: HttpURLConnection?=null
            try {
                val response =StringBuffer()
                val url = URL("http://192.168.137.1:8080/novels/novel.txt")
                connection=url.openConnection() as HttpURLConnection
                connection.connectTimeout=8000
                connection.readTimeout=8000
                val input=connection.inputStream
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                showResponse(response.toString())
            }catch (e:Exception) {
                e.printStackTrace()
            }finally {
                connection?.disconnect()
            }
        }
    }
    private fun showResponse(response: String) {
        runOnUiThread {
            testiv.text = response
            novelStr = response
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_novel)
    }
}