package com.example.dmzj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Selection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.chapter_elem_layout.view.*
import kotlinx.android.synthetic.main.comment_elem_layout.view.*
import kotlinx.android.synthetic.main.comment_footer_layout.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.inputmethod.EditorInfo
import android.widget.*
import kotlinx.android.synthetic.main.activity_novel_details.*
import kotlinx.android.synthetic.main.activity_novel_details.etv
import kotlinx.android.synthetic.main.activity_novel_details.iv_back
import kotlinx.android.synthetic.main.activity_novel_details.iv_cartoon_cover
import kotlinx.android.synthetic.main.activity_novel_details.recyclerviewChapter
import kotlinx.android.synthetic.main.activity_novel_details.recyclerviewComment
import kotlinx.android.synthetic.main.activity_novel_details.tv_correct_order
import kotlinx.android.synthetic.main.activity_novel_details.tv_details_title
import kotlinx.android.synthetic.main.activity_novel_details.tv_reverse_order
import kotlin.math.log


class NovelDetailsActivity : AppCompatActivity() {
    private val commentList: ArrayList<CommentInfo> =ArrayList<CommentInfo>()
    var nowCommentUserName="姬野游星"
    var nowCommentUserSex=1
    var nowCommentUserHeadURL="jiyeyouxing"
    var nowCommentUserID=0

    lateinit var nowNovelInfo:NovelInfo
    var comment_info:String=""
    var comment_date:String=""

    //    读评论区
    private fun getCommentListBySendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getComment.jsp?comment_objects_name=" + URLEncoder.encode(nowNovelInfo.novelName)
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
            commentList.clear()
            //json数组
            val jsonArray= JSONArray(jsonData)
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)

                val userName=jsonObject.getString("userName")
                val commentInfo=jsonObject.getString("commentInfo")
                val commentDate=jsonObject.getString("commentDate")
                val commentObjectName=jsonObject.getString("commentObjectName")
                val userHeadURL=jsonObject.getString("userHeadURL")
                val userID:Int=jsonObject.getString("userID").toInt()
                val userSex:Int=jsonObject.getString("userSex").toInt()
                val commentLikeNum:Int=jsonObject.getString("commentLikeNum").toInt()

//                此处的Log打印出的已是null

//                Log.d("NovelDetailsActivity","打印信息：$userName")
//                Toast.makeText(this,cartoonDescription,Toast.LENGTH_LONG).show()
                commentList.add(CommentInfo(userName,commentInfo,commentDate,commentObjectName,userHeadURL,
                    userID,userSex,commentLikeNum))
            }
            runOnUiThread(){
                val commentLayoutManager = LinearLayoutManager(this)
                recyclerviewComment.layoutManager =commentLayoutManager
                var editText:EditText=findViewById(R.id.et_comment)
                val commentAdapter = CommentRecycleAdapter(commentList,editText)
                recyclerviewComment.adapter=commentAdapter
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    //    发送评论的HttpURLConnection连接
    private fun sendAddCommentByRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()

                val urlStr:String="http://192.168.137.1:8080/addComment.jsp"+
                        "?user_name="+URLEncoder.encode(nowCommentUserName)+
                        "&user_ID=" +URLEncoder.encode(nowCommentUserID.toString())+
                        "&comment_info="+URLEncoder.encode(comment_info)+
                        "&comment_date="+URLEncoder.encode(comment_date)+
                        "&comment_objects_name="+URLEncoder.encode(nowNovelInfo.novelName)+
                        "&user_sex="+URLEncoder.encode(nowCommentUserSex.toString())+
                        "&comment_like_num="+URLEncoder.encode("0")+
                        "&user_head_url="+URLEncoder.encode(nowCommentUserHeadURL)
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
                getCommentListBySendRequestWithHttpURLConnection()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }

    //    查询订阅
    fun getIsNovelSubscribeInfoBySendRequestWithHttpURLConnection(){
        thread {
            var connection: HttpURLConnection? = null
            try {
                initCommentUser()
                Log.d("哼","nowCommentUserID:"+nowCommentUserID)
                Log.d("哼","nowNovelInfo.novelName:"+nowNovelInfo.novelName)

                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getIsNovelSubscribe.jsp?user_ID="+nowCommentUserID+
                        "&subscribe_novel_name="+nowNovelInfo.novelName
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
                getIsNovelSubscribeInfoRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun getIsNovelSubscribeInfoRefresh(jsonData:String){
        try {
            val data= java.util.ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                val isSubscribed:Int=jsonObject.getString("isSubscribed").toInt()
                if(isSubscribed>=1){
                    runOnUiThread(){
                        btn_novel_subscribe.text = "取消订阅"
                    }
                    Log.d("哼","运行到了")
                }
                Log.d("哼","没有上句就是没运行到")
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    //    订阅
    private fun addNovelSubscribeInfoBySendRequestWithHttpURLConnection(){
        thread {
            var connection: HttpURLConnection? = null
            try {
                initCommentUser()
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/addNovelSubscribeInfo.jsp?user_ID="+nowCommentUserID+
                        "&subscribe_novel_name="+nowNovelInfo.novelName
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
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    //    取消订阅
    private fun deleteNovelSubscribeInfoBySendRequestWithHttpURLConnection(){
        thread {
            var connection: HttpURLConnection? = null
            try {
                initCommentUser()
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/deleteNovelSubscribeInfo.jsp?user_ID="+nowCommentUserID+
                        "&subscribe_novel_name="+nowNovelInfo.novelName
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
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }



    fun initCommentUser(){
        val globalUser: GlobalUser = application as GlobalUser
        nowCommentUserName=globalUser.userName
        nowCommentUserSex=globalUser.userSex
        nowCommentUserHeadURL=globalUser.userHeadURL
        nowCommentUserID=globalUser.userID
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val globalUser: GlobalUser = application as GlobalUser
        initCommentUser()
        nowNovelInfo  = intent.getSerializableExtra("nowNovelInfo") as NovelInfo
//        nowNovelInfo=NovelInfo("s","s","s","s","daojianshenyu","s","s",
//        1,1,1,1,1,1)
        getCommentListBySendRequestWithHttpURLConnection()
        getIsNovelSubscribeInfoBySendRequestWithHttpURLConnection()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_novel_details)


//        注册章节recyclerview
        val layoutManager = GridLayoutManager(this, 2) //第二个参数为网格的列数
        recyclerviewChapter.layoutManager =layoutManager
        val adapter = ChapterRecycleAdapter(nowNovelInfo.novelChapterNum,nowNovelInfo)
        recyclerviewChapter.adapter=adapter
//
//
//
//        注册评论区recyclerview
//        val commentLayoutManager = LinearLayoutManager(this)
//        recyclerviewComment.layoutManager =commentLayoutManager
//        var editText:EditText=findViewById(R.id.et_comment)
//        val commentAdapter = CommentRecycleAdapter(commentList,editText)
//        recyclerviewComment.adapter=commentAdapter
        val imgUrl = "http://192.168.137.1:8080/novel_cover/"+nowNovelInfo.novelCoverURL+".png"
        val image : ImageView =iv_cartoon_cover
        val context: Context = image.context
        Glide.with(context).load(imgUrl).dontAnimate().into(image)
        tv_details_title.text=nowNovelInfo.novelName
        iv_novel_update_author.text=nowNovelInfo.novelAuthor
        iv_novel_update_cartoonType.text=nowNovelInfo.novelType
        iv_novel_update_cartoonHot.text=iv_novel_update_cartoonHot.text.toString()+" "+nowNovelInfo.novelHot
        iv_novel_update_cartoon_subscribe.text=iv_novel_update_cartoon_subscribe.text.toString()+" "+nowNovelInfo.novelSubscriptionsNum
        if (nowNovelInfo.novelIsFinished==0)
            iv_novel_update_updateTime.text=nowNovelInfo.novelLastUpdateTime+" 连载中"
        else
            iv_novel_update_updateTime.text=nowNovelInfo.novelLastUpdateTime+" 已完结"

        etv.text=nowNovelInfo.novelDescription
        initViews()
    }


    //  章节列表
//    容器
    private class ChapterRecycleViewHolder(view: View) : RecyclerView.ViewHolder(view){}
    //    适配器
    private class ChapterRecycleAdapter(val chapterNum:Int,val nowPageInfo:NovelInfo ): RecyclerView.Adapter<ChapterRecycleViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return chapterNum
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterRecycleViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.chapter_elem_layout,parent,false)
            return ChapterRecycleViewHolder(view)
        }
        override fun onBindViewHolder(holder: ChapterRecycleViewHolder, position: Int) {
            holder.itemView.btn_test.text= "第"+(position+1)+"话"

            holder.itemView.btn_test.setOnClickListener(){
//                Toast.makeText(holder.itemView.context,"click",Toast.LENGTH_LONG).show()
                val intent= Intent(holder.itemView.context,MyNovelsReadActivity::class.java)
                intent.putExtra("nowNovelInfo",nowPageInfo)
                intent.putExtra("nowChapter",position+1)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    //评论区

    private class CommentRecycleViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        val et_comment:EditText =view.findViewById(R.id.et_comment)

    }
    //    适配器
    private class CommentRecycleAdapter(val commentList:List<CommentInfo> ,var editText: EditText): RecyclerView.Adapter<CommentRecycleViewHolder>(){
        var likeStyleList: ArrayList<Boolean> =ArrayList<Boolean>()
        //      列表行数
        override fun getItemCount(): Int {
            return commentList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRecycleViewHolder {
            for(index in 0 .. commentList.size){
                likeStyleList.add(false)
            }
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.comment_elem_layout,parent,false)
            return CommentRecycleViewHolder(view)
        }
        override fun onBindViewHolder(holder: CommentRecycleViewHolder, position: Int) {
            val info =commentList[position]
            holder.itemView.item_info_iv_reply.text=info.commentInfo
            holder.itemView.item_info_iv_user_name.text=info.userName
            holder.itemView.item_info_iv_reply_time.text=info.commentDate
//            头像
            val imgUrl = "http://192.168.137.1:8080/user_head/"+info.userHeadURL+".png"
            val image :ImageView= holder.itemView.item_info_iv
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)
//            性别
            if(info.userSex==0)
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
            holder.itemView.setOnClickListener()
            {
                editText.text.clear()
                val ss = SpannableString("@"+info.userName+": ")
                ss.setSpan( ForegroundColorSpan(Color.rgb(0,191,255)), 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                editText.setText(ss)
                editText.setFocusable(true)
                Selection.setSelection(editText.getText(),editText.getText().toString().length)
            }

        }
    }



    fun initViews()
    {
//        章节正逆序
        tv_correct_order.setTextColor(Color.rgb(0,191,255))
        tv_correct_order.setOnClickListener(){
            tv_correct_order.setTextColor(Color.rgb(0,191,255))
            tv_reverse_order.setTextColor(Color.rgb(0,0,0))
        }
        tv_reverse_order.setOnClickListener(){
            tv_reverse_order.setTextColor(Color.rgb(0,191,255))
            tv_correct_order.setTextColor(Color.rgb(0,0,0))
        }

        //      返回按钮
        iv_back.setOnClickListener()
        {
            this.finish()
        }
//      订阅
        btn_novel_subscribe.setOnClickListener()
        {
            initCommentUser()
            if(nowCommentUserID==0){
                var  intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
            else {
                if (btn_novel_subscribe.text == "订阅漫画") {
                    addNovelSubscribeInfoBySendRequestWithHttpURLConnection()
                    btn_novel_subscribe.text = "取消订阅"
                    btn_novel_subscribe.isSelected = false
                    val toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
                    toast.setText("订阅成功~")
                    toast.show()
                } else {
                    deleteNovelSubscribeInfoBySendRequestWithHttpURLConnection()
                    btn_novel_subscribe.isSelected = true
                    btn_novel_subscribe.text = "订阅漫画"
                }
            }
        }
//        跳转到漫画阅读界面
        btn_novel_continue_read.setOnClickListener()
        {
            val intent= Intent(this,MyNovelsReadActivity::class.java)
            intent.putExtra("nowNovelInfo",nowNovelInfo)
            intent.putExtra("nowChapter",1)
            startActivity(intent)
        }
//        发送评论
        btn_add_comment.setOnClickListener() {
            initCommentUser()
            if (nowCommentUserID == 0) {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val curDate = Date(System.currentTimeMillis())
                val format = dateFormat.format(curDate)
                comment_date = format
                comment_info = et_comment.text.toString()

                sendAddCommentByRequestWithHttpURLConnection()
//            sendRequestWithHttpURLConnection()
                et_comment.setText("")
                val toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
                toast.setText("评论成功~")
                toast.show()//            关闭键盘
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                val v = window.peekDecorView()
                if (null != v) {
                    //强制隐藏键盘
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
//        键盘发送评论
        et_comment.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                initCommentUser()
                if (nowCommentUserID == 0) {
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val curDate = Date(System.currentTimeMillis())
                    val format = dateFormat.format(curDate)
                    comment_date = format
                    comment_info = et_comment.text.toString()
                    sendAddCommentByRequestWithHttpURLConnection()
                    et_comment.setText("")
                    val toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
                    toast.setText("评论成功~")
                    toast.show()
                }
                return@OnEditorActionListener false //返回true，保留软键盘。false，隐藏软键盘
            }
            false
        })

    }

}