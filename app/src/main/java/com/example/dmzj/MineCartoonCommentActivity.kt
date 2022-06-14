package com.example.dmzj

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_mine_cartoon_comment.*
import kotlinx.android.synthetic.main.mine_comment_elem_layout.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MineCartoonCommentActivity : AppCompatActivity() {
    var nowUserID=0
    private val cartoonList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
    private val commentList:ArrayList<CommentInfo> =ArrayList<CommentInfo>()
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getCartoonComment.jsp?user_ID="+nowUserID
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
                cartoonList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
                ))

                val userName=jsonObject.getString("userName")
                val commentInfo=jsonObject.getString("commentInfo")
                val commentDate=jsonObject.getString("commentDate")
                val commentObjectName=jsonObject.getString("commentObjectName")
                val userHeadURL=jsonObject.getString("userHeadURL")
                val userID:Int=jsonObject.getString("userID").toInt()
                val userSex:Int=jsonObject.getString("userSex").toInt()
                val commentLikeNum:Int=jsonObject.getString("commentLikeNum").toInt()

                commentList.add(CommentInfo(userName,commentInfo,commentDate,commentObjectName,userHeadURL,
                    userID,userSex,commentLikeNum))
            }
            runOnUiThread(){
                val layoutManager= LinearLayoutManager(this)
                recyclerviewMineComment.layoutManager =layoutManager
                val adapter = cartoonCommentAdapter(cartoonList,commentList)
                recyclerviewMineComment.adapter=adapter
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
        setContentView(R.layout.activity_mine_cartoon_comment)
        sendRequestWithHttpURLConnection()
        iv_back.setOnClickListener()
        {
            this.finish()
        }
    }

    //    容器
    private class cartoonCommentViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }
    //    适配器
    private class cartoonCommentAdapter(val cartoonList: List<CartoonsInfo> ,val commentList: List<CommentInfo>): RecyclerView.Adapter<cartoonCommentViewHolder>(){
        var likeStyleList: ArrayList<Boolean> =ArrayList<Boolean>()
        //      列表行数
        override fun getItemCount(): Int {
            return cartoonList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):cartoonCommentViewHolder {
            for(index in 0 .. commentList.size){
                likeStyleList.add(false)
            }
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.mine_comment_elem_layout,parent,false)
            return cartoonCommentViewHolder(view)
        }
        override fun onBindViewHolder(holder: cartoonCommentViewHolder, position: Int) {
//            设置封面和标题
            val info = cartoonList[position]
            val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+info.cartoonCoverURL+".png"
            val image :ImageView= holder.itemView.iv_comment_cover
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)
            holder.itemView.tv_comment_title.text=info.cartoonName

//            设置评论区
            val info2 =commentList[position]
            holder.itemView.item_info_iv_reply.text=info2.commentInfo
            holder.itemView.item_info_iv_user_name.text=info2.userName
            holder.itemView.item_info_iv_reply_time.text=info2.commentDate
//            头像
            val imgUrl2 = "http://192.168.137.1:8080/user_head/"+info2.userHeadURL+".png"
            val image2 :ImageView= holder.itemView.item_info_iv
            val Context2: Context = image.context
            Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)
//            性别
            if(info2.userSex==0)
                holder.itemView.iv_user_sex.setImageResource(R.drawable.sex_female)
            else
                holder.itemView.iv_user_sex.setImageResource(R.drawable.sex_male)
//          点赞操作
            holder.itemView.iv_like_style.setOnClickListener() {
                if (likeStyleList[position] == false) {
                    holder.itemView.iv_like_style.setImageResource(R.drawable.like_true)
                    holder.itemView.tv_like_num.text =
                        (holder.itemView.tv_like_num.text.toString().toInt() + 1).toString()
                    likeStyleList[position] = true
                } else {
                    holder.itemView.iv_like_style.setImageResource(R.drawable.like_false)
                    holder.itemView.tv_like_num.text =
                        (holder.itemView.tv_like_num.text.toString().toInt() - 1).toString()
                    likeStyleList[position] = false
                }
            }
//          点击封面跳转
            holder.itemView.iv_comment_cover.setOnClickListener() {
                val intent = Intent(holder.itemView.context, CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", cartoonList[position])
                holder.itemView.context.startActivity(intent)
            }
//          点击评论区跳转
            holder.itemView.rl_comment_info.setOnClickListener() {
                val intent = Intent(holder.itemView.context, CommentDetailActivity::class.java)
                intent.putExtra("CommentInfo", commentList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}