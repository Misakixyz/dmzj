package com.example.dmzj


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_update.*

import kotlin.concurrent.thread

import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.header_layout_cartoons.*
//import kotlinx.android.synthetic.main.page_layout.view.*
import kotlinx.android.synthetic.main.update_elem_layout.view.*


//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class CartoonUpdateActivity : AppCompatActivity() {
    private val cartoonsUpdateList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getCartoonUpdate.jsp"
                val url = URL(urlStr)
                connection = url.openConnection() as HttpURLConnection
//                connection.setDoOutput(true);
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type", "application/json");
//                connection.setRequestProperty("contentType", "utf-8");
//                connection.setRequestProperty("Accept-Charset", "utf-8");
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
//                showResponse(response.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun showResponse(response: String) {
        runOnUiThread {
            tv_update.text = response
        }
    }
    private fun sendRequestByHttpUrl2(){
        thread {
            try {
                val client = OkHttpClient()
                //构造数据传送体
                //构建请求
                val request = Request.Builder()
                    .url("http://192.168.137.1:8080/cartoonInfo.jsp")
                    .build()
                //执行
                val response= client.newCall(request).execute()
                //得到返回值
                val responseData=response.body?.string()

                if(responseData!= null){
                    refresh(responseData.toString())
                }else{
                    Toast.makeText(this,"null",Toast.LENGTH_LONG).show()
                }

            }
            catch (e: Exception){
                e.printStackTrace()
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
//                此处的Log打印出的已是null

                Log.d("UpdateActivity","打印信息：$cartoonDescription")
//                Toast.makeText(this,cartoonDescription,Toast.LENGTH_LONG).show()
                cartoonsUpdateList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished

                ))
            }
            runOnUiThread(){
                val layoutManager= LinearLayoutManager(this)
                recyclerviewCartoonUpdate.layoutManager =layoutManager
                val adapter = cartoonsUpdateAdapter(cartoonsUpdateList)
                recyclerviewCartoonUpdate.adapter=adapter
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
        setContentView(R.layout.activity_update)
//        sendRequestByHttpUrl2()

        initViews()

    }
    //    容器
    private class cartoonsUpdateViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val cartoonName:TextView = view.findViewById(R.id.iv_cartoons_update_cartoonName)
        val author:TextView = view.findViewById(R.id.iv_cartoons_update_author)
        val cartoonType:TextView = view.findViewById(R.id.iv_cartoons_update_cartoonType)
        val updateTime:TextView = view.findViewById(R.id.iv_cartoons_update_updateTime)
        val chapter:TextView = view.findViewById(R.id.iv_cartoons_update_chapter)


    }
    //    适配器
    private class cartoonsUpdateAdapter(val cartoonsUpdateList: List<CartoonsInfo>): RecyclerView.Adapter<cartoonsUpdateViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return cartoonsUpdateList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):cartoonsUpdateViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.update_elem_layout,parent,false)
            return cartoonsUpdateViewHolder(view)
        }
        override fun onBindViewHolder(holder: cartoonsUpdateViewHolder, position: Int) {
//            if(info.imgSrc==("update_p1"))
//                holder.imgSrc.setImageResource(R.drawable.update_p1)
//            if(info.imgSrc==("update_p2"))
//                holder.imgSrc.setImageResource(R.drawable.update_p2)
//            if(info.imgSrc==("update_p3"))
//                holder.imgSrc.setImageResource(R.drawable.update_p3)
//            if(info.imgSrc==("update_p4"))
//                holder.imgSrc.setImageResource(R.drawable.update_p4)

//          识别页码，编码从1开始
            // 总页数
//            val pageNum=4
//            for (index in 0..pageNum+1) {
//                var srcStr:String="update_p"+index.toString()
//                if (info.imgSrc == srcStr)
//                    holder.imgSrc.setImageResource(R.drawable.update_p1+index-1)
//            }
//            holder.itemView.setOnClickListener(){
//                val intent =Intent( holder.itemView.context,CartoonsDetailsActivity::class.java)
//                intent.putExtra("Name", holder.itemView.iv_cartoons_update_cartoonName.text)
//                holder.itemView.context.startActivity(intent)
//            }
            val info = cartoonsUpdateList[position]
            val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+info.cartoonCoverURL+".png"
            val image :ImageView= holder.itemView.iv_cartoons_update_imgSrc
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)

            holder.cartoonName.text=info.cartoonName
            holder.author.text=info.cartoonAuthor
            holder.cartoonType.text=info.cartoonType
            holder.updateTime.text=info.cartoonLastUpdateTime
            holder.chapter.text="第"+info.cartoonChapterNum.toString()+"话"


            holder.itemView.setOnClickListener() {
//                Toast.makeText(holder.itemView.context,cartoonsUpdateList[position].cartoonDescription,Toast.LENGTH_LONG).show()
                val intent = Intent(holder.itemView.context, CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", cartoonsUpdateList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }



    private fun initViews() {
        // footer
        val btnFooterCartoons:ImageView =findViewById(R.id.iv_footer_cartoons)
        val btnFooterNews:ImageView =findViewById(R.id.iv_footer_news)
        val btnFooterNovels:ImageView =findViewById(R.id.iv_footer_novels)
        val btnFooterMine:ImageView =findViewById(R.id.iv_footer_mine)
        // header
        val btnHeaderRecommend:TextView =findViewById(R.id.tv_header_recommend)
        val btnHeaderClassification:TextView =findViewById(R.id.tv_header_classification)
        val btnHeaderRank:TextView =findViewById(R.id.tv_header_rank)
//        val btnHeaderWelfare:TextView =findViewById(R.id.tv_header_welfare)
        val btnHeaderUpdate:TextView =findViewById(R.id.tv_update)

        // 返回顶部按钮
        val recycleviewCartoonsUpdate:RecyclerView=findViewById(R.id.recyclerviewCartoonUpdate)
        val btnGoToTop:ImageView=findViewById(R.id.iv_gototop)
        // xml 中默认所有字体图片为灰色
        // 设置页首当前界面字体颜色为蓝色
        // android:textColor="#00BFFF"
        btnHeaderUpdate.setTextColor(Color.rgb(0,191,255))
        //设置页首光标
//            TODO()
        // 设置页脚当前界面图片颜色为蓝色
        btnFooterCartoons.setImageResource(R.drawable.footer_cartoons_blue)



        // footer listener.
        // 漫画界面的绑定
        btnFooterCartoons.setOnClickListener()
        {
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        // 新闻界面的绑定
        btnFooterNews.setOnClickListener()
        {
            val intent =Intent(this,NewsActivity::class.java)
            startActivity(intent)
        }
        // 轻小说界面的绑定
        btnFooterNovels.setOnClickListener()
        {
            val intent =Intent(this,NovelActivity::class.java)
            startActivity(intent)
        }
        // 我的界面的绑定
        btnFooterMine.setOnClickListener()
        {
//            val intent =Intent(this,MineActivity::class.java)
//            startActivity(intent)
            val intent =Intent(this,MineActivity::class.java)
            startActivity(intent)
        }


        // header listener
        // 推荐界面的绑定
        btnHeaderRecommend.setOnClickListener()
        {
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        // 分类界面的绑定
        btnHeaderClassification.setOnClickListener()
        {
            val intent =Intent(this,CartoonClassificationActivity::class.java)
            startActivity(intent)
        }
        // 排行界面的绑定
        btnHeaderRank.setOnClickListener()
        {
            val intent =Intent(this,CartoonRankActivity::class.java)
            startActivity(intent)
        }
        // 更新界面的绑定
        btnHeaderUpdate.setOnClickListener()
        {
            val intent =Intent(this,CartoonUpdateActivity::class.java)
            startActivity(intent)
        }
        // 福利界面的绑定
//        btnHeaderWelfare.setOnClickListener()
//        {
//            val intent =Intent(this,WelfareActivity::class.java)
//            startActivity(intent)
//        }
        iv_header_search.setOnClickListener(){
            val intent =Intent(this,SearchActivity::class.java)
            intent.putExtra("searchType","CartoonUpdateActivity")
            startActivity(intent)
        }
        // 监听RecycleView界面，判断是否位于顶部，位于顶部则隐藏按钮
//        recycleviewCartoonsUpdate.setOnTouchListener()
//        {
//
//        }


        // 返回顶部按钮
        btnGoToTop.setOnClickListener()
        {
//            recyclerviewCartoonUpdate.scrollToPosition(0)
//          平滑的滚动
            recyclerviewCartoonUpdate.smoothScrollToPosition(0)
        }

    }


}
