package com.example.dmzj

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_mine_cartoon_subscribe.*
import kotlinx.android.synthetic.main.activity_mine_cartoon_subscribe.iv_back
import kotlinx.android.synthetic.main.subscribe_elem_layout.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MineCartoonSubscribeActivity : AppCompatActivity() {
    var nowUserID=0
    private val cartoonSubscribeList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getCartoonSubscribe.jsp?user_ID="+nowUserID
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
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)

                val cartoonName=jsonObject.getString("cartoonName")
                val cartoonDescription=jsonObject.getString("cartoonDescription")
                val cartoonAuthor=jsonObject.getString("cartoonAuthor")
                val cartoonURL=jsonObject.getString("cartoonURL")
                val cartoonCoverURL=jsonObject.getString("cartoonCoverURL")
                val cartoonLastUpdateTime=jsonObject.getString("cartoonLastUpdateTime")
                val cartoonType=jsonObject.getString("cartoonType")


                val cartoonID:Int=jsonObject.getString("cartoonID").toInt()
                val cartoonChapterNum:Int=jsonObject.getString("cartoonChapterNum").toInt()
                val cartoonHot:Int=jsonObject.getString("cartoonHot").toInt()
                val cartoonSubscriptionsNum:Int=jsonObject.getString("cartoonSubscriptionsNum").toInt()
                val cartoonCommentNum:Int=jsonObject.getString("cartoonCommentNum").toInt()
                val cartoonIsFinished:Int=jsonObject.getString("cartoonIsFinished").toInt()
                cartoonSubscribeList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
                ))
            }
            runOnUiThread(){
                val layoutManager = GridLayoutManager(this, 3) //第二个参数为网格的列数
                recyclerviewMineCartoonSubscribe.layoutManager =layoutManager
                val adapter = cartoonSubscribeAdapter(cartoonSubscribeList)
                recyclerviewMineCartoonSubscribe.adapter=adapter
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
        setContentView(R.layout.activity_mine_cartoon_subscribe)
        sendRequestWithHttpURLConnection()
        iv_back.setOnClickListener()
        {
            this.finish()
        }
    }

    //    容器
    private class cartoonSubscribeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val cartoonName:TextView = view.findViewById(R.id.tv_subscribe_name)
        val author:TextView = view.findViewById(R.id.tv_subscribe_author)
    }
    //    适配器
    private class cartoonSubscribeAdapter(val cartoonSubscribeList: List<CartoonsInfo>): RecyclerView.Adapter<cartoonSubscribeViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return cartoonSubscribeList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):cartoonSubscribeViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.subscribe_elem_layout,parent,false)
            return cartoonSubscribeViewHolder(view)
        }
        override fun onBindViewHolder(holder: cartoonSubscribeViewHolder, position: Int) {
            val info = cartoonSubscribeList[position]
            val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+info.cartoonCoverURL+".png"
            val image :ImageView= holder.itemView.iv_subscribe_cover
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)

            holder.cartoonName.text=info.cartoonName
            holder.author.text=info.cartoonAuthor

            holder.itemView.setOnClickListener() {
//                Toast.makeText(holder.itemView.context,cartoonSubscribeList[position].cartoonDescription,Toast.LENGTH_LONG).show()
                val intent = Intent(holder.itemView.context, CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", cartoonSubscribeList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}