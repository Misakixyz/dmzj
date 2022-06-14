package com.example.dmzj

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.search_result_elem_layout.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern
import kotlin.concurrent.thread

class SearchResultActivity : AppCompatActivity() {
    private val cartoonSearchResultList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
    private val novelSearchResultList: ArrayList<NovelInfo> =ArrayList<NovelInfo>()

    private val selectCartoonSearchResultList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
    private val selectNovelSearchResultList: ArrayList<NovelInfo> =ArrayList<NovelInfo>()
    lateinit var searchType:String
    lateinit var searchKeywords:String
    private lateinit var pull_refresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        searchType = intent.getStringExtra("searchType").toString()
        searchKeywords = intent.getStringExtra("searchKeywords").toString()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search_result)
        pullRefreshInit()
        initViews()
        diss.setOnClickListener(){
            if(searchType=="MainActivity") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else if(searchType=="CartoonUpdateActivity") {
                val intent = Intent(this, CartoonUpdateActivity::class.java)
                startActivity(intent)
            }
            else if(searchType=="CartoonClassificationActivity") {
                val intent = Intent(this, CartoonClassificationActivity::class.java)
                startActivity(intent)
            }
            else if(searchType=="CartoonRankActivity") {
                val intent = Intent(this, CartoonRankActivity::class.java)
                startActivity(intent)
            }
            else if(searchType=="novel") {
                val intent = Intent(this, NovelActivity::class.java)
                startActivity(intent)
            }
            else{
                finish()
            }
        }
    }
    fun search(){
        searchKeywords=et_name.text.toString()
        if(searchType.equals("NovelActivity")){
            selectNovelSearchResultList.clear()
            for(index in novelSearchResultList) {
                for(i in searchKeywords){
                    //              匹配作者
                    val contentAuthor =index.novelAuthor
                    val patternAuthor = ".*${i}.*"
                    val isMatchAuthor: Boolean = Pattern.matches(patternAuthor, contentAuthor)
                    if(isMatchAuthor==true)
                    {
                        selectNovelSearchResultList.add(index)
                        break
                    }
                    //                匹配名字
                    val contentName =index.novelName
                    val patternName = ".*${i}.*"
                    val isMatchName: Boolean = Pattern.matches(patternName, contentName)
                    if(isMatchName==true)
                    {
                        selectNovelSearchResultList.add(index)
                        break
                    }
                }
            }
            val layoutManager= LinearLayoutManager(this)
            recyclerviewSearchResult.layoutManager =layoutManager
            val adapter = novelSearchResultAdapter(selectNovelSearchResultList)
            recyclerviewSearchResult.adapter=adapter
        }
        else{
            selectCartoonSearchResultList.clear()
            for(index in cartoonSearchResultList) {
                for(i in searchKeywords){
                    //                匹配作者
                    val contentAuthor =index.cartoonAuthor
                    val patternAuthor = ".*${i}.*"
                    val isMatchAuthor: Boolean = Pattern.matches(patternAuthor, contentAuthor)
                    if(isMatchAuthor==true)
                    {
                        selectCartoonSearchResultList.add(index)
                        break
                    }
                    //                匹配名字
                    val contentName =index.cartoonName
                    val patternName = ".*${i}.*"
                    val isMatchName: Boolean = Pattern.matches(patternName, contentName)
                    if(isMatchName==true)
                    {
                        selectCartoonSearchResultList.add(index)
                        break
                    }
                }
            }
            val layoutManager= LinearLayoutManager(this)
            recyclerviewSearchResult.layoutManager =layoutManager
            val adapter = cartoonSearchResultAdapter(selectCartoonSearchResultList)
            recyclerviewSearchResult.adapter=adapter
        }
    }
    private fun initViews(){
        et_name.setText(searchKeywords)
        if(searchType.equals("MainActivity")||
            searchType.equals("CartoonUpdateActivity")||
            searchType.equals("CartoonClassificationActivity")||
            searchType.equals("CartoonRankActivity")) {
            getCartoonSearchResultBySendRequestWithHttpURLConnection()
        }
        else
            getNovelSearchResultSendRequestWithHttpURLConnection()
//        按下回车搜索
        et_name.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            false
        }
    }
    private fun pullRefreshInit(){
        pull_refresh = findViewById<SwipeRefreshLayout>(R.id.pull_refresh)
        pull_refresh.setColorSchemeColors(Color.parseColor("#00BFFF"))
        pull_refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                runOnUiThread{
                    search()
                    pull_refresh.isRefreshing = false
                }
            }
        }
    }
    private fun getCartoonSearchResultBySendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getCartoonUpdate.jsp"
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
                getCartoonSearchResultRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun getCartoonSearchResultRefresh(jsonData:String){
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
                cartoonSearchResultList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished

                ))
            }
            runOnUiThread(){
                search()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    private fun getNovelSearchResultSendRequestWithHttpURLConnection() {
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
                getNovelSearchResultRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun getNovelSearchResultRefresh(jsonData:String){
        try {
            val data=ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            novelSearchResultList.clear()
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

                novelSearchResultList.add(
                    NovelInfo(novelName,novelDescription,novelAuthor,novelURL,novelCoverURL,novelLastUpdateTime,novelType,
                        novelID,novelChapterNum,novelHot,novelSubscriptionsNum,novelCommentNum,novelIsFinished
                    )
                )
            }
            runOnUiThread(){
                search()
            }

        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    private class searchResultViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val Name: TextView = view.findViewById(R.id.tv_search_result_name)
        val author: TextView = view.findViewById(R.id.tv_search_result_author)
        val Type: TextView = view.findViewById(R.id.tv_search_result_type)
        val chapter: TextView = view.findViewById(R.id.tv_search_result_new_chapter)

    }
    //    漫画适配器
    private class cartoonSearchResultAdapter(val searchResultList: List<CartoonsInfo>): RecyclerView.Adapter<searchResultViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return searchResultList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchResultViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.search_result_elem_layout,parent,false)
            return searchResultViewHolder(view)
        }
        override fun onBindViewHolder(holder:searchResultViewHolder, position: Int) {
            val info = searchResultList[position]
//            卡通适配
                val imgUrl =
                    "http://192.168.137.1:8080/cartoon_cover/" + info.cartoonCoverURL + ".png"
                val image: ImageView = holder.itemView.iv_search_result_cover
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)

                holder.Name.text = info.cartoonName
                holder.author.text = info.cartoonAuthor
                holder.Type.text = info.cartoonType
                holder.chapter.text = "第" + info.cartoonChapterNum.toString() + "话"

                holder.itemView.setOnClickListener() {
                    val intent =
                        Intent(holder.itemView.context, CartoonsDetailsActivity::class.java)
                    intent.putExtra("nowCartoonsInfo", searchResultList[position])
                    holder.itemView.context.startActivity(intent)
                }
        }
    }
    //   小说适配器
    private class novelSearchResultAdapter(val searchResultList: List<NovelInfo>): RecyclerView.Adapter<searchResultViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return searchResultList.size
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchResultViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.search_result_elem_layout,parent,false)
            return searchResultViewHolder(view)
        }
        override fun onBindViewHolder(holder:searchResultViewHolder, position: Int) {
            val info = searchResultList[position]
//            漫画

                val imgUrl =
                    "http://192.168.137.1:8080/novel_cover/" + info.novelCoverURL + ".png"
                val image: ImageView = holder.itemView.iv_search_result_cover
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)

                holder.Name.text = info.novelName
                holder.author.text = info.novelAuthor
                holder.Type.text = info.novelType
                holder.chapter.text = "第" + info.novelChapterNum.toString() + "话"

                holder.itemView.setOnClickListener() {
                    val intent =
                        Intent(holder.itemView.context, NovelDetailsActivity::class.java)
                    intent.putExtra("nowNovelInfo", searchResultList[position])
                    holder.itemView.context.startActivity(intent)
                }
        }
    }

}