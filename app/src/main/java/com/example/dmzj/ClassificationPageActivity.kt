package com.example.dmzj

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_classification_page.*
import kotlinx.android.synthetic.main.classification_elem_layout.view.*

import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread


class ClassificationPageActivity : AppCompatActivity() {
    lateinit var cartoonType:String
    private val cartoonsClassificationPageList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getCartoonClassification.jsp?cartoon_type=" + URLEncoder.encode(cartoonType)
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
            val data=ArrayList<CommentInfo>()
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
                cartoonsClassificationPageList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
                ))
            }
            Log.d("tag","$cartoonsClassificationPageList")
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        cartoonType = intent.getStringExtra("cartoonType").toString()
        sendRequestWithHttpURLConnection()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_classification_page)

        val layoutManager = GridLayoutManager(this, 3) //第二个参数为网格的列数
        recyclerviewCartoonClassificationPage.layoutManager =layoutManager
        val adapter = CartoonsClassificationPageAdapter(cartoonsClassificationPageList)
        recyclerviewCartoonClassificationPage.adapter=adapter
        initViews()

    }
    //    容器
    private class CartoonsClassificationPageViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imgSrc:ImageView = view.findViewById(R.id.cartoons_classification_cartoonsbook_src)
        val cartoonName:TextView = view.findViewById(R.id.cartoons_classification_cartoonsbook_bookname)
        val author:TextView = view.findViewById(R.id.cartoons_classification_cartoonsbook_author)


    }
    //    适配器
    private class CartoonsClassificationPageAdapter(val cartoonsClassificationPageList: List<CartoonsInfo>): RecyclerView.Adapter<CartoonsClassificationPageViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return cartoonsClassificationPageList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CartoonsClassificationPageViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.classification_elem_layout,parent,false)
            return CartoonsClassificationPageViewHolder(view)
        }
        override fun onBindViewHolder(holder: CartoonsClassificationPageViewHolder, position: Int) {
            val info = cartoonsClassificationPageList[position]
            holder.cartoonName.text=info.cartoonName
            holder.author.text=info.cartoonAuthor
            val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+info.cartoonCoverURL+".png"
            val image :ImageView= holder.itemView.cartoons_classification_cartoonsbook_src
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)
            holder.itemView.setOnClickListener(){
                val intent =
                    Intent(holder.itemView.context, CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", cartoonsClassificationPageList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }
//    初始化
    private fun initViews() {
        tv_update_order.setTextColor(Color.rgb(0,191,255))
    }
}
