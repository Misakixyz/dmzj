package com.example.dmzj

// 当你下次打开这个kt文件的时候，只需要通过intent传输一个NewsInfo类的对象即可。注释掉相应的内容即可。
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_read.*
import kotlinx.android.synthetic.main.activity_news_read.iv_back
import kotlinx.android.synthetic.main.activity_news_read.recyclerviewComment
import kotlinx.android.synthetic.main.comment_elem_layout.view.*
import kotlinx.android.synthetic.main.comment_footer_layout.*
import org.json.JSONArray
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter
import org.sufficientlysecure.htmltextview.HtmlTextView
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

class NewsReadActivity : AppCompatActivity() {
    private val commentList: ArrayList<CommentInfo> =ArrayList<CommentInfo>()
    var nowNewsInfo:NewsInfo=NewsInfo("新闻1","比利","jiyeyouxing","news1","jiyeyouxing","2021-12-21","",1,1,1,1)
    var nowCommentUserName=""
    var nowCommentUserSex=1
    var nowCommentUserHeadURL=""
    var nowCommentUserID=0
    var comment_info:String=""
    var comment_date:String=""

    //    获取章节的小说内容
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/news_html/"+nowNewsInfo.newsURL+".txt"
                val url = URL(urlStr)
                connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                val input = connection.inputStream
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                        response.append("\n")
                    }
                }
                showResponse(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun showResponse(response: String) {
        runOnUiThread {
            val htmlTextView = findViewById<View>(R.id.html_text) as HtmlTextView
            var html_url=response
            htmlTextView.setHtml(
                html_url,
                HtmlHttpImageGetter(htmlTextView)
            )
        }
    }


    //    读评论区
    private fun getCommentListBySendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getComment.jsp?comment_objects_name=" + URLEncoder.encode(nowNewsInfo.newsName)
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

//                Log.d("CartoonsDetailsActivity","打印信息：$userName")
//                Toast.makeText(this,cartoonDescription,Toast.LENGTH_LONG).show()
                commentList.add(CommentInfo(userName,commentInfo,commentDate,commentObjectName,userHeadURL,
                    userID,userSex,commentLikeNum))
            }
            runOnUiThread(){
                val commentLayoutManager = LinearLayoutManager(this)
                recyclerviewComment.layoutManager =commentLayoutManager
                var editText: EditText =findViewById(R.id.et_comment)
                val commentAdapter = CommentRecycleAdapter(commentList, editText)
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
                        "?user_name="+ URLEncoder.encode(nowCommentUserName)+
                        "&user_ID=" + URLEncoder.encode(nowCommentUserID.toString())+
                        "&comment_info="+ URLEncoder.encode(comment_info)+
                        "&comment_date="+ URLEncoder.encode(comment_date)+
                        "&comment_objects_name="+ URLEncoder.encode(nowNewsInfo.newsName)+
                        "&user_sex="+ URLEncoder.encode(nowCommentUserSex.toString())+
                        "&comment_like_num="+ URLEncoder.encode("0")+
                        "&user_head_url="+ URLEncoder.encode(nowCommentUserHeadURL)
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
    override fun onCreate(savedInstanceState: Bundle?) {
        initCommentUser()
        nowNewsInfo= intent.getSerializableExtra("nowNewsInfo") as NewsInfo
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_read)
        sendRequestWithHttpURLConnection()
        supportActionBar?.hide()
        getCommentListBySendRequestWithHttpURLConnection()
        initNewsInfo()
        initViews()
    }
    fun initNewsInfo(){
//        新闻标题
//        tv_news_title.text=nowNewsInfo.newsName
//        更新头像
        val imgUrl = "http://192.168.137.1:8080/news_author_head/"+nowNewsInfo.newsAuthorHeadURL+".png"
        val image : ImageView =iv_news_author_head
        val context: Context = image.context
        Glide.with(context).load(imgUrl).dontAnimate().into(image)
//        作者名字
        tv_news_author_name.text=nowNewsInfo.newsAuthor
//        更新时间
        tv_news_time.text=nowNewsInfo.newsTime
    }
    fun initCommentUser(){
        val globalUser: GlobalUser = application as GlobalUser
        nowCommentUserName=globalUser.userName
        nowCommentUserSex=globalUser.userSex
        nowCommentUserHeadURL=globalUser.userHeadURL
        nowCommentUserID=globalUser.userID
    }
    //评论区
    private class CommentRecycleViewHolder(view: View) : RecyclerView.ViewHolder(view){}
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
            holder.itemView.tv_like_num.text=info.commentLikeNum.toString()
            //头像
            val imgUrl = "http://192.168.137.1:8080/user_head/"+info.userHeadURL+".png"
            val image : ImageView = holder.itemView.item_info_iv
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)
//            性别
            if(info.userSex==0)
                holder.itemView.iv_user_sex.setImageResource(R.drawable.sex_female)
            else
                holder.itemView.iv_user_sex.setImageResource(R.drawable.sex_male)
//          点赞操作
            holder.itemView.iv_like_style.setOnClickListener(){
                if(likeStyleList[position]==false) {
                    holder.itemView.iv_like_style.setImageResource(R.drawable.like_true)
                    holder.itemView.tv_like_num.text =
                        (holder.itemView.tv_like_num.text.toString().toInt() + 1).toString()
                    likeStyleList[position]=true
                }
                else{
                    holder.itemView.iv_like_style.setImageResource(R.drawable.like_false)
                    holder.itemView.tv_like_num.text =
                        (holder.itemView.tv_like_num.text.toString().toInt() - 1).toString()
                    likeStyleList[position]=false
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

    fun initViews(){
        iv_back.setOnClickListener()
        {
            this.finish()
        }
        //        发送评论
        btn_add_comment.setOnClickListener(){
            initCommentUser()
            if(nowCommentUserID==0){
                var  intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
            else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val curDate = Date(System.currentTimeMillis())
                val format = dateFormat.format(curDate)
                comment_date = format
//            Toast.makeText(this,"观察"+comment_date+"空格",Toast.LENGTH_LONG).show()
                comment_info = et_comment.text.toString()
//

                sendAddCommentByRequestWithHttpURLConnection()
                et_comment.setText("")
                val toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
                toast.setText("评论成功~")
                toast.show()
//            关闭键盘
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                val v = window.peekDecorView()
                if (null != v) {
                    //强制隐藏键盘
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
//        键盘区的发送
        et_comment.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                initCommentUser()
                if(nowCommentUserID==0){
                    var  intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                }
                else {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val curDate = Date(System.currentTimeMillis())
                    val format = dateFormat.format(curDate)
                    comment_date = format
//            Toast.makeText(this,"观察"+comment_date+"空格",Toast.LENGTH_LONG).show()
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