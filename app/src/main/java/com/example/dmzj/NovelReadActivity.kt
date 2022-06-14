package com.example.dmzj
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_novel_read.*
import kotlinx.android.synthetic.main.page_novel_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.net.HttpURLConnection
import java.net.URL
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.activity_novel_read.btn_cartoon_return
import kotlinx.android.synthetic.main.page_novel_layout.onei
import java.io.*
import kotlin.concurrent.thread


class NovelReadActivity : Activity() {
    var novelStr:String=""
    private fun sendRequestWithHttpURLConnection(){
        thread{
            var connection:HttpURLConnection?=null
            try {
                val response =StringBuffer()
                val url = URL("http://192.168.137.1:8080/novels/novel.txt")
                connection=url.openConnection() as HttpURLConnection
                connection.connectTimeout=8000
                connection.readTimeout=8000
                val input=connection.inputStream
                val reader =BufferedReader(InputStreamReader(input))
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
    private fun showResponse(response: String)
    {
        runOnUiThread{
            onei.text=response
            novelStr=response
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_read)
        vp.setOnClickListener()
        {
            btn_cartoon_return.isVisible = true
        }
        initViews()
        sendRequestWithHttpURLConnection()
        vp.adapter = RecyclerViewAdapter(novelStr,this)
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})
    }

    private fun initViews() {
//        返回主界面按钮
        val btnReturn: Button = findViewById(R.id.btn_cartoon_return)
        btnReturn.setOnClickListener()
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    class ViewData(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class RecyclerViewAdapter(val novelStr:String ,val mcontext:Context) : RecyclerView.Adapter<ViewData>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewData {
            return ViewData(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.page_novel_layout, parent, false)
            )
        }

        //            @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("RestrictedApi")
        override fun onBindViewHolder(holder: ViewData, position: Int) {
//                val current = LocalDateTime.now() //用这个会有报错，主要是java.time.LocalDateTime包的问题。
            //获取系统时间
//                val fileName = "D:/Android/dmzj/app/src/main/res/novel.text"
//                var noveltext=File(fileName).readText()


            val dateFormat = SimpleDateFormat("HH:mm")
            val curDate = Date(System.currentTimeMillis())
            val format = dateFormat.format(curDate)
            holder.itemView.read_time_TV.text = format
            // 页码：
            var strpage: String = (position + 1).toString() + "/" + getItemCount().toString() + "  "
            holder.itemView.read_countNum_TV.text = strpage
            // 内容
//            holder.itemView.onei.text=novelStr
//            Toast.makeText(mcontext,"int",Toast.LENGTH_LONG).show()
//            holder.itemView.onei.getLineBounds()


//                when(position){
//                    0 -> {
//                        holder.itemView.onei.text=getNovel()
//                    }
//                    1 ->
//                    {
//                        holder.itemView.onei.text=getNovel()
//                    }
//                    2  -> {
////
//                    }
//                }
//            holder.itemView.onei.setOnClickListener()
//            {
//                holder.itemView.btn_cartoon_return.isVisible =
//                    holder.itemView.btn_cartoon_return.isVisible != true
//
//            }
//            holder.itemView.btn_cartoon_return.setOnClickListener()
//            {
////                val intent =Intent(mcontext,NovelActivity::class.java)
////                startActivity(intent)
//                getActivity(mcontext)?.finish()
//            }
        }

        // 为了运行。。。
//        private fun startActivity(intent: Intent) {
//            TODO("Not yet implemented")
//        }

        override fun getItemCount(): Int {
            return 16
        }
    }
}

// 为了运行
//private fun Intent.setClass(
//    recyclerViewAdapter: NovelReadActivity.RecyclerViewAdapter,
//    java: Class<CartoonReadActivity>
//) {
//
//}


