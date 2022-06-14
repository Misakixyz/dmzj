package com.example.dmzj

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_go_novel.*
import kotlinx.android.synthetic.main.novel_new_elem_layout.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class GoNovelActivity : AppCompatActivity() {
    private val novelNewList: ArrayList<NovelInfo> =ArrayList<NovelInfo>()
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getNovelNew.jsp"
                val url = URL(urlStr)
                connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                val input = connection.inputStream
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                refresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun refresh(jsonData:String){
        try {
            val data=ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            novelNewList.clear()
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)

                val novelName=jsonObject.getString("novelName")
                val novelDescription=jsonObject.getString("novelDescription")
                val novelAuthor=jsonObject.getString("novelAuthor")
                val novelURL=jsonObject.getString("novelURL")
                val novelCoverURL=jsonObject.getString("novelCoverURL")
                val novelLastUpdateTime=jsonObject.getString("novelLastUpdateTime")
                val novelType=jsonObject.getString("novelType")


                val novelID:Int=jsonObject.getString("novelID").toInt()
                val novelChapterNum:Int=jsonObject.getString("novelChapterNum").toInt()
                val novelHot:Int=jsonObject.getString("novelHot").toInt()
                val novelSubscriptionsNum:Int=jsonObject.getString("novelSubscriptionsNum").toInt()
                val novelCommentNum:Int=jsonObject.getString("novelCommentNum").toInt()
                val novelIsFinished:Int=jsonObject.getString("novelIsFinished").toInt()
//                此处的Log打印出的已是null

                Log.d("UpdateActivity","打印信息：$novelDescription")
//                Toast.makeText(this,novelDescription,Toast.LENGTH_LONG).show()
                novelNewList.add(
                    NovelInfo(novelName,novelDescription,novelAuthor,novelURL,novelCoverURL,novelLastUpdateTime,novelType,
                        novelID,novelChapterNum,novelHot,novelSubscriptionsNum,novelCommentNum,novelIsFinished
                    )
                )
            }
            runOnUiThread(){
                val layoutManager= LinearLayoutManager(this)
                recyclerviewNovelNew.layoutManager =layoutManager
                val adapter = NovelNewAdapter(novelNewList)
                recyclerviewNovelNew.adapter=adapter
            }

        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        sendRequestWithHttpURLConnection()
        super.onCreate(savedInstanceState)
        // 隐藏标题栏
        supportActionBar?.hide()
        setContentView(R.layout.activity_go_novel)
        initViews()
    }
    //    容器
    private class NovelNewViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val novelName: TextView = view.findViewById(R.id.iv_novel_new_novelName)
        val author: TextView = view.findViewById(R.id.iv_novel_new_author)
        val novelType: TextView = view.findViewById(R.id.iv_novel_new_novelType)
        val updateTime: TextView = view.findViewById(R.id.iv_novel_new_updateTime)
        val chapter: TextView = view.findViewById(R.id.iv_novel_new_chapter)
    }
    //    适配器
    private class NovelNewAdapter(val novelNewList: List<NovelInfo>): RecyclerView.Adapter<NovelNewViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return novelNewList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):NovelNewViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.novel_new_elem_layout,parent,false)
            return NovelNewViewHolder(view)
        }
        override fun onBindViewHolder(holder: NovelNewViewHolder, position: Int) {
            val info = novelNewList[position]
            val imgUrl = "http://192.168.137.1:8080/novel_cover/"+info.novelCoverURL+".png"
            val image : ImageView = holder.itemView.iv_novel_new_imgSrc
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)

            holder.novelName.text=info.novelName
            holder.author.text=info.novelAuthor
            holder.novelType.text=info.novelType
            holder.updateTime.text=info.novelLastUpdateTime
            holder.chapter.text="第"+info.novelChapterNum.toString()+"话"


            holder.itemView.setOnClickListener() {
                val intent = Intent(holder.itemView.context, NovelDetailsActivity::class.java)
                intent.putExtra("nowNovelInfo", novelNewList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }
    private fun initViews() {

    }


}