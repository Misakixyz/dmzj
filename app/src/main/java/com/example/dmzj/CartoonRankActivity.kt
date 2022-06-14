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
import kotlinx.android.synthetic.main.activity_cartoons_rank.*
import kotlinx.android.synthetic.main.cartoons_rank_elem_layout.view.*
import kotlinx.android.synthetic.main.header_layout_cartoons.*
import kotlinx.android.synthetic.main.update_elem_layout.view.*
import kotlinx.android.synthetic.main.update_elem_layout.view.iv_cartoons_update_imgSrc
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class CartoonRankActivity : AppCompatActivity() {
    var orderByType="cartoon_hot"
    private val cartoonsHotRankList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getCartoonRank.jsp?orderByType="+orderByType
                val url = URL(urlStr)
                connection = url.openConnection() as HttpURLConnection
                connection.setRequestMethod("GET")
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
    private fun refresh(jsonData:String){
        try {
            val data=ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            cartoonsHotRankList.clear()
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
                cartoonsHotRankList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished

                ))
            }
            runOnUiThread(){
                val layoutManager= LinearLayoutManager(this)
                recyclerviewCartoonRank.layoutManager =layoutManager
                val adapter = cartoonsHotRankAdapter(cartoonsHotRankList)
                recyclerviewCartoonRank.adapter=adapter
            }

        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        orderByType="cartoon_hot"
        sendRequestWithHttpURLConnection()
        super.onCreate(savedInstanceState)
        // 隐藏标题栏
        supportActionBar?.hide()
        setContentView(R.layout.activity_cartoons_rank)
        initViews()
//        val layoutManager= LinearLayoutManager(this)
//        recyclerviewCartoonRank.layoutManager =layoutManager
//        val adapter = cartoonsHotRankAdapter(cartoonsHotRankList)
//        recyclerviewCartoonRank.adapter=adapter
    }
    //    容器
    private class cartoonsHotRankViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imgSrc:ImageView = view.findViewById(R.id.iv_cartoons_update_imgSrc)
        val cartoonName:TextView = view.findViewById(R.id.iv_cartoons_update_cartoonName)
        val author:TextView = view.findViewById(R.id.iv_cartoons_update_author)
        val cartoonType:TextView = view.findViewById(R.id.iv_cartoons_update_cartoonType)
        val updateTime:TextView = view.findViewById(R.id.iv_cartoons_update_updateTime)
//        val chapter:TextView = view.findViewById(R.id.iv_cartoons_update_chapter)
    }
    //    适配器
    private class cartoonsHotRankAdapter(val cartoonsHotRankList: List<CartoonsInfo>): RecyclerView.Adapter<cartoonsHotRankViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return cartoonsHotRankList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):cartoonsHotRankViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.cartoons_rank_elem_layout,parent,false)
            return cartoonsHotRankViewHolder(view)
        }
        override fun onBindViewHolder(holder: cartoonsHotRankViewHolder, position: Int) {
            val info = cartoonsHotRankList[position]
            val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+info.cartoonCoverURL+".png"
            val image :ImageView= holder.itemView.iv_cartoons_update_imgSrc
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)

            holder.cartoonName.text=info.cartoonName
            holder.author.text=info.cartoonAuthor
            holder.cartoonType.text=info.cartoonType
            holder.updateTime.text=info.cartoonLastUpdateTime
//            holder.chapter.text="第"+info.cartoonChapterNum.toString()+"话"
            holder.itemView.tv_cartoon_rank.text=(position+1).toString()
            if(position==0){
                holder.itemView.iv_cartoon_rank_bg.setBackgroundResource(R.drawable.rank_top)
            }
            if(position==1){
                holder.itemView.iv_cartoon_rank_bg.setBackgroundResource(R.drawable.rank_second)
            }
            if(position==2){
                holder.itemView.iv_cartoon_rank_bg.setBackgroundResource(R.drawable.rank_third)
            }
            if(position>=3){
                holder.itemView.iv_cartoon_rank_bg.setBackgroundResource(R.drawable.rank_fourth)
            }
            holder.itemView.setOnClickListener() {
//                Toast.makeText(holder.itemView.context,cartoonsUpdateList[position].cartoonDescription,Toast.LENGTH_LONG).show()
                val intent = Intent(holder.itemView.context, CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", cartoonsHotRankList[position])
//                intent.putExtra("cartoonName", cartoonsHotRankList[position].cartoonName)
//                intent.putExtra(
//                    "cartoonDescription",
//                    cartoonsHotRankList[position].cartoonDescription
//                )
//                intent.putExtra("cartoonAuthor", cartoonsHotRankList[position].cartoonAuthor)
//                intent.putExtra("cartoonURL", cartoonsHotRankList[position].cartoonURL)
//                intent.putExtra("cartoonCoverURL", cartoonsHotRankList[position].cartoonCoverURL)
//                intent.putExtra(
//                    "cartoonLastUpdateTime",
//                    cartoonsHotRankList[position].cartoonLastUpdateTime
//                )
//                intent.putExtra("cartoonType", cartoonsHotRankList[position].cartoonType)
//
//
//                intent.putExtra("cartoonID", cartoonsHotRankList[position].cartoonID)
//                intent.putExtra("cartoonChapterNum", cartoonsHotRankList[position].cartoonChapterNum)
//                intent.putExtra("cartoonHot", cartoonsHotRankList[position].cartoonHot   )
//                intent.putExtra("cartoonSubscriptionsNum", cartoonsHotRankList[position].cartoonSubscriptionsNum)
//                intent.putExtra("cartoonCommentNum", cartoonsHotRankList[position].cartoonCommentNum)
//                intent.putExtra("cartoonIsFinished", cartoonsHotRankList[position].cartoonIsFinished)

                holder.itemView.context.startActivity(intent)
            }
        }
    }
    private fun initViews() {
        tv_order_by_hot.setTextColor(Color.rgb(0,191,255))
        tv_order_by_hot.setOnClickListener(){
            orderByType="cartoon_hot"
            sendRequestWithHttpURLConnection()
            tv_order_by_hot.setTextColor(Color.rgb(0,191,255))
            tv_order_by_comment_num.setTextColor(Color.rgb(0,0,0))
            tv_order_by_subscriptions_num.setTextColor(Color.rgb(0,0,0))
        }
        tv_order_by_comment_num.setOnClickListener(){
            orderByType="cartoon_comment_num"
            sendRequestWithHttpURLConnection()
            tv_order_by_hot.setTextColor(Color.rgb(0,0,0))
            tv_order_by_comment_num.setTextColor(Color.rgb(0,191,255))
            tv_order_by_subscriptions_num.setTextColor(Color.rgb(0,0,0))
        }
        tv_order_by_subscriptions_num.setOnClickListener(){
            orderByType="cartoon_subscriptions_num"
            sendRequestWithHttpURLConnection()
            tv_order_by_hot.setTextColor(Color.rgb(0,0,0))
            tv_order_by_comment_num.setTextColor(Color.rgb(0,0,0))
            tv_order_by_subscriptions_num.setTextColor(Color.rgb(0,191,255))
        }











        // footer
        val btnFooterCartoons: ImageView =findViewById(R.id.iv_footer_cartoons)
        val btnFooterNews: ImageView =findViewById(R.id.iv_footer_news)
        val btnFooterNovels: ImageView =findViewById(R.id.iv_footer_novels)
        val btnFooterMine: ImageView =findViewById(R.id.iv_footer_mine)
        // header
        val btnHeaderRecommend: TextView =findViewById(R.id.tv_header_recommend)
        val btnHeaderUpdate: TextView =findViewById(R.id.tv_update)
        val btnHeaderClassification: TextView =findViewById(R.id.tv_header_classification)
        val btnHeaderRank: TextView =findViewById(R.id.tv_header_rank)
//        val btnHeaderWelfare: TextView =findViewById(R.id.tv_header_welfare)
        // xml 中默认所有字体图片为灰色
        // 设置页首当前界面字体颜色为蓝色
        // android:textColor="#00BFFF"
        btnHeaderRank.setTextColor(Color.rgb(0,191,255))
        //设置页首光标
//            TODO()
        // 设置页脚当前界面图片颜色为蓝色
        btnFooterCartoons.setImageResource(R.drawable.footer_cartoons_blue)

        // footer listener.
        // 漫画界面的绑定
        btnFooterCartoons.setOnClickListener()
        {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        // 新闻界面的绑定
        btnFooterNews.setOnClickListener()
        {
            val intent = Intent(this,NewsActivity::class.java)
            startActivity(intent)
        }
        // 轻小说界面的绑定
        btnFooterNovels.setOnClickListener()
        {
            val intent = Intent(this,NovelActivity::class.java)
            startActivity(intent)
        }
        // 我的界面的绑定
        btnFooterMine.setOnClickListener()
        {
            val intent = Intent(this,MineActivity::class.java)
            startActivity(intent)
        }


        // header listener
        // 推荐界面的绑定
        btnHeaderRecommend.setOnClickListener()
        {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        // 分类界面的绑定
        btnHeaderClassification.setOnClickListener()
        {
            val intent = Intent(this,CartoonClassificationActivity::class.java)
            startActivity(intent)
        }
        // 排行界面的绑定
        btnHeaderRank.setOnClickListener()
        {
            val intent = Intent(this,CartoonRankActivity::class.java)
            startActivity(intent)
        }
        // 更新界面的绑定
        btnHeaderUpdate.setOnClickListener()
        {
            val intent = Intent(this,CartoonUpdateActivity::class.java)
            startActivity(intent)
        }
        // 福利界面的绑定
//        btnHeaderWelfare.setOnClickListener()
//        {
//            val intent = Intent(this,WelfareActivity::class.java)
//            startActivity(intent)
//        }
        iv_header_search.setOnClickListener(){
            val intent =Intent(this,SearchActivity::class.java)
            intent.putExtra("searchType","CartoonRankActivity")
            startActivity(intent)
        }
    }


}