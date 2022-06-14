package com.example.dmzj

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_mine_novel_subscribe.*
import kotlinx.android.synthetic.main.activity_mine_novel_subscribe.iv_back
import kotlinx.android.synthetic.main.subscribe_elem_layout.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MineNovelSubscribeActivity : AppCompatActivity() {
    var nowUserID=0
    private val novelSubscribeList: ArrayList<NovelInfo> =ArrayList<NovelInfo>()
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getNovelSubscribe.jsp?user_ID="+nowUserID
                val url = URL(urlStr)
                connection = url.openConnection() as HttpURLConnection
                connection.setRequestMethod("GET")
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
            novelSubscribeList.clear()
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
                novelSubscribeList.add(
                    NovelInfo(novelName,novelDescription,novelAuthor,novelURL,novelCoverURL,novelLastUpdateTime,novelType,
                        novelID,novelChapterNum,novelHot,novelSubscriptionsNum,novelCommentNum,novelIsFinished
                    )
                )
            }
            runOnUiThread(){
                val layoutManager = GridLayoutManager(this, 3) //第二个参数为网格的列数
                recyclerviewMineNovelSubscribe.layoutManager =layoutManager
                val adapter = novelSubscribeAdapter(novelSubscribeList)
                recyclerviewMineNovelSubscribe.adapter=adapter
            }

        }catch (e: Exception){
            e.printStackTrace()
        }

    }


    fun initCommentUser(){
        val globalUser: GlobalUser = application as GlobalUser
        nowUserID=globalUser.userID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initCommentUser()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_mine_novel_subscribe)
        sendRequestWithHttpURLConnection()
        iv_back.setOnClickListener()
        {
            this.finish()
        }
    }

    //    容器
    private class novelSubscribeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val novelName:TextView = view.findViewById(R.id.tv_subscribe_name)
        val author:TextView = view.findViewById(R.id.tv_subscribe_author)
    }
    //    适配器
    private class novelSubscribeAdapter(val novelSubscribeList: List<NovelInfo>): RecyclerView.Adapter<novelSubscribeViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return novelSubscribeList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):novelSubscribeViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.subscribe_elem_layout,parent,false)
            return novelSubscribeViewHolder(view)
        }
        override fun onBindViewHolder(holder: novelSubscribeViewHolder, position: Int) {
            val info = novelSubscribeList[position]
            val imgUrl = "http://192.168.137.1:8080/novel_cover/"+info.novelCoverURL+".png"
            val image :ImageView= holder.itemView.iv_subscribe_cover
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)

            holder.novelName.text=info.novelName
            holder.author.text=info.novelAuthor

            holder.itemView.setOnClickListener() {
                val intent = Intent(holder.itemView.context, NovelDetailsActivity::class.java)
                intent.putExtra("nowNovelInfo", novelSubscribeList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}