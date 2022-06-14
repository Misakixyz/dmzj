package com.example.dmzj

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_novel.*
import kotlinx.android.synthetic.main.activity_novel.banner
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class NovelActivity : AppCompatActivity() {
    lateinit var recentUpdateNovel1:NovelInfo
    lateinit var recentUpdateNovel2:NovelInfo
    lateinit var recentUpdateNovel3:NovelInfo
    lateinit var cartooningNovel1:NovelInfo
    lateinit var cartooningNovel2:NovelInfo
    lateinit var cartooningNovel3:NovelInfo
    lateinit var willBeCartoonfulNovel1:NovelInfo
    lateinit var willBeCartoonfulNovel2:NovelInfo
    lateinit var willBeCartoonfulNovel3:NovelInfo
    lateinit var classMustLookNovel1:NovelInfo
    lateinit var classMustLookNovel2:NovelInfo
    lateinit var classMustLookNovel3:NovelInfo
    lateinit var classMustLookNovel4:NovelInfo
    lateinit var classMustLookNovel5:NovelInfo
    lateinit var classMustLookNovel6:NovelInfo
    private lateinit var pull_refresh: SwipeRefreshLayout

    //    onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
//        var userapp: GlobalUser = application as GlobalUser
//        Log.d("测试",userapp.userName)
//        Log.d("测试22222222",userapp.userBloodType.toString())
        getRecentUpdateNovelInfoSendRequestWithHttpURLConnection()
        getCartooningNovelInfoSendRequestWithHttpURLConnection()
        getWillBeCartoonfulNovelInfoSendRequestWithHttpURLConnection()
        getClassMustLookNovelInfoSendRequestWithHttpURLConnection()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_novel)
        initViews()
        bannerInit()
        novelCoverInit()
        novelFunInit()
        pullRefreshInit()
    }
//    下拉刷新
    private fun pullRefreshInit(){
        pull_refresh = findViewById<SwipeRefreshLayout>(R.id.pull_refresh)
        pull_refresh.setColorSchemeColors(Color.parseColor("#00BFFF"))
        pull_refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                runOnUiThread{
                    bannerInit()
                    getRecentUpdateNovelInfoSendRequestWithHttpURLConnection()
                    getCartooningNovelInfoSendRequestWithHttpURLConnection()
                    getWillBeCartoonfulNovelInfoSendRequestWithHttpURLConnection()
                    getClassMustLookNovelInfoSendRequestWithHttpURLConnection()
                    pull_refresh.isRefreshing = false
                }
            }
        }
    }
//    最近更新
    private fun getRecentUpdateNovelInfoSendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getNovelInfo.jsp"
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
                getRecentUpdateNovelInfoRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun getRecentUpdateNovelInfoRefresh(jsonData:String){
        try {
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<NovelInfo> =ArrayList<NovelInfo>()
            tempList.clear()
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
                tempList.add(NovelInfo(novelName,novelDescription,novelAuthor,novelURL,novelCoverURL,novelLastUpdateTime,novelType,
                    novelID,novelChapterNum,novelHot,novelSubscriptionsNum,novelCommentNum,novelIsFinished
                ))
            }
//            Log.d("tag","测试信息：$tempList")
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            recentUpdateNovel1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            var randomNum2=random.nextInt(tempList.size)
            recentUpdateNovel2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            var randomNum3=random.nextInt(tempList.size)
            recentUpdateNovel3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            runOnUiThread(){
                tv_novel_recent_update1_name.text=recentUpdateNovel1.novelName
                tv_novel_recent_update2_name.text=recentUpdateNovel2.novelName
                tv_novel_recent_update3_name.text=recentUpdateNovel3.novelName
                tv_novel_recent_update1_author.text=recentUpdateNovel1.novelAuthor
                tv_novel_recent_update2_author.text=recentUpdateNovel2.novelAuthor
                tv_novel_recent_update3_author.text=recentUpdateNovel3.novelAuthor

                val imgUrl = "http://192.168.137.1:8080/novel_cover/"+recentUpdateNovel1.novelCoverURL+".png"
                val image :ImageView= iv_novel_recent_update1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)


                val imgUrl2 = "http://192.168.137.1:8080/novel_cover/"+recentUpdateNovel2.novelCoverURL+".png"
                val image2 :ImageView= iv_novel_recent_update2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/novel_cover/"+recentUpdateNovel3.novelCoverURL+".png"
                val image3 :ImageView= iv_novel_recent_update3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
//    动画化
    private fun getCartooningNovelInfoSendRequestWithHttpURLConnection() {
    thread {
        var connection: HttpURLConnection? = null
        try {
            val response = StringBuffer()
            val urlStr:String="http://192.168.137.1:8080/getNovelInfo.jsp"
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
            getCartooningNovelInfoRefresh(response.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
    }
}
    private fun getCartooningNovelInfoRefresh(jsonData:String){
        try {
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<NovelInfo> =ArrayList<NovelInfo>()
            tempList.clear()
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
                tempList.add(NovelInfo(novelName,novelDescription,novelAuthor,novelURL,novelCoverURL,novelLastUpdateTime,novelType,
                    novelID,novelChapterNum,novelHot,novelSubscriptionsNum,novelCommentNum,novelIsFinished
                ))
            }
//            Log.d("tag","测试信息：$tempList")
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            cartooningNovel1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            var randomNum2=random.nextInt(tempList.size)
            cartooningNovel2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            var randomNum3=random.nextInt(tempList.size)
            cartooningNovel3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            runOnUiThread(){
                tv_novel_cartooning1_name.text=cartooningNovel1.novelName
                tv_novel_cartooning2_name.text=cartooningNovel2.novelName
                tv_novel_cartooning3_name.text=cartooningNovel3.novelName
                tv_novel_cartooning1_author.text=cartooningNovel1.novelAuthor
                tv_novel_cartooning2_author.text=cartooningNovel2.novelAuthor
                tv_novel_cartooning3_author.text=cartooningNovel3.novelAuthor

                val imgUrl = "http://192.168.137.1:8080/novel_cover/"+cartooningNovel1.novelCoverURL+".png"
                val image :ImageView= iv_novel_cartooning1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)


                val imgUrl2 = "http://192.168.137.1:8080/novel_cover/"+cartooningNovel2.novelCoverURL+".png"
                val image2 :ImageView= iv_novel_cartooning2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/novel_cover/"+cartooningNovel3.novelCoverURL+".png"
                val image3 :ImageView= iv_novel_cartooning3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
//    即将动画化
    private fun getWillBeCartoonfulNovelInfoSendRequestWithHttpURLConnection() {
    thread {
        var connection: HttpURLConnection? = null
        try {
            val response = StringBuffer()
            val urlStr:String="http://192.168.137.1:8080/getNovelInfo.jsp"
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
            getWillBeCartoonfulNovelInfoRefresh(response.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
    }
}
    private fun getWillBeCartoonfulNovelInfoRefresh(jsonData:String){
        try {
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<NovelInfo> =ArrayList<NovelInfo>()
            tempList.clear()
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
                tempList.add(NovelInfo(novelName,novelDescription,novelAuthor,novelURL,novelCoverURL,novelLastUpdateTime,novelType,
                    novelID,novelChapterNum,novelHot,novelSubscriptionsNum,novelCommentNum,novelIsFinished
                ))
            }
//            Log.d("tag","测试信息：$tempList")
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            willBeCartoonfulNovel1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            var randomNum2=random.nextInt(tempList.size)
            willBeCartoonfulNovel2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            var randomNum3=random.nextInt(tempList.size)
            willBeCartoonfulNovel3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            runOnUiThread(){
                tv_novel_will_be_cartoonful1_name.text=willBeCartoonfulNovel1.novelName
                tv_novel_will_be_cartoonful2_name.text=willBeCartoonfulNovel2.novelName
                tv_novel_will_be_cartoonful3_name.text=willBeCartoonfulNovel3.novelName
                tv_novel_will_be_cartoonful1_author.text=willBeCartoonfulNovel1.novelAuthor
                tv_novel_will_be_cartoonful2_author.text=willBeCartoonfulNovel2.novelAuthor
                tv_novel_will_be_cartoonful3_author.text=willBeCartoonfulNovel3.novelAuthor

                val imgUrl = "http://192.168.137.1:8080/novel_cover/"+willBeCartoonfulNovel1.novelCoverURL+".png"
                val image :ImageView= iv_novel_will_be_cartoonful1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)


                val imgUrl2 = "http://192.168.137.1:8080/novel_cover/"+willBeCartoonfulNovel2.novelCoverURL+".png"
                val image2 :ImageView= iv_novel_will_be_cartoonful2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/novel_cover/"+willBeCartoonfulNovel3.novelCoverURL+".png"
                val image3 :ImageView= iv_novel_will_be_cartoonful3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
//    经典必看
    private fun getClassMustLookNovelInfoSendRequestWithHttpURLConnection() {
    thread {
        var connection: HttpURLConnection? = null
        try {
            val response = StringBuffer()
            val urlStr:String="http://192.168.137.1:8080/getNovelInfo.jsp"
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
            getClassMustLookNovelInfoRefresh(response.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
    }
}
    private fun getClassMustLookNovelInfoRefresh(jsonData:String){
        try {
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<NovelInfo> =ArrayList<NovelInfo>()
            tempList.clear()
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
                tempList.add(NovelInfo(novelName,novelDescription,novelAuthor,novelURL,novelCoverURL,novelLastUpdateTime,novelType,
                    novelID,novelChapterNum,novelHot,novelSubscriptionsNum,novelCommentNum,novelIsFinished
                ))
            }
//            Log.d("tag","测试信息：$tempList")
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            classMustLookNovel1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            var randomNum2=random.nextInt(tempList.size)
            classMustLookNovel2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            var randomNum3=random.nextInt(tempList.size)
            classMustLookNovel3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            var randomNum4=random.nextInt(tempList.size)
            classMustLookNovel4=tempList[randomNum4]
            tempList.removeAt(randomNum4)
            var randomNum5=random.nextInt(tempList.size)
            classMustLookNovel5=tempList[randomNum5]
            tempList.removeAt(randomNum5)
            var randomNum6=random.nextInt(tempList.size)
            classMustLookNovel6=tempList[randomNum6]
            tempList.removeAt(randomNum6)


            runOnUiThread(){
                tv_novel_class_must_look1_name.text=classMustLookNovel1.novelName
                tv_novel_class_must_look2_name.text=classMustLookNovel2.novelName
                tv_novel_class_must_look3_name.text=classMustLookNovel3.novelName
                tv_novel_class_must_look4_name.text=classMustLookNovel4.novelName
                tv_novel_class_must_look5_name.text=classMustLookNovel5.novelName
                tv_novel_class_must_look6_name.text=classMustLookNovel6.novelName


                tv_novel_class_must_look1_author.text=classMustLookNovel1.novelAuthor
                tv_novel_class_must_look2_author.text=classMustLookNovel2.novelAuthor
                tv_novel_class_must_look3_author.text=classMustLookNovel3.novelAuthor
                tv_novel_class_must_look4_author.text=classMustLookNovel4.novelAuthor
                tv_novel_class_must_look5_author.text=classMustLookNovel5.novelAuthor
                tv_novel_class_must_look6_author.text=classMustLookNovel6.novelAuthor

                val imgUrl = "http://192.168.137.1:8080/novel_cover/"+classMustLookNovel1.novelCoverURL+".png"
                val image :ImageView= iv_novel_class_must_look1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)


                val imgUrl2 = "http://192.168.137.1:8080/novel_cover/"+classMustLookNovel2.novelCoverURL+".png"
                val image2 :ImageView= iv_novel_class_must_look2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/novel_cover/"+classMustLookNovel3.novelCoverURL+".png"
                val image3 :ImageView= iv_novel_class_must_look3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

                val imgUrl4 = "http://192.168.137.1:8080/novel_cover/"+classMustLookNovel4.novelCoverURL+".png"
                val image4 :ImageView= iv_novel_class_must_look4
                val Context4: Context = image.context
                Glide.with(Context4).load(imgUrl4).dontAnimate().into(image4)


                val imgUrl5 = "http://192.168.137.1:8080/novel_cover/"+classMustLookNovel5.novelCoverURL+".png"
                val image5 :ImageView= iv_novel_class_must_look5
                val Context5: Context = image.context
                Glide.with(Context5).load(imgUrl5).dontAnimate().into(image5)


                val imgUrl6 = "http://192.168.137.1:8080/novel_cover/"+classMustLookNovel6.novelCoverURL+".png"
                val image6 :ImageView= iv_novel_class_must_look6
                val Context6: Context = image.context
                Glide.with(Context6).load(imgUrl6).dontAnimate().into(image6)

            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    private fun bannerInit(){
        val arrayImageUrl = arrayListOf<String>("http://192.168.137.1:8080/banner_cover/5.png",
            "http://192.168.137.1:8080/banner_cover/9.png",
            "http://192.168.137.1:8080/banner_cover/6.png",
            "http://192.168.137.1:8080/banner_cover/8.png",
            "http://192.168.137.1:8080/banner_cover/5.png")
        val arrayTitle = arrayListOf<String>("剧场版动画《名侦探柯南 万圣节的新娘》新预告", "TV动画《86-不存在的战区-》22与23话延期播出",
            "SE手游《Gate of Nightmares》漫画版开始连载","周五榕树下·三连回顾第九期","P站美图推荐--画师特辑")

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(MyLoader())
        //设置图片网址或地址的集合
        banner.setImages(arrayImageUrl)
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default)
        //设置轮播图的标题集合
        banner.setBannerTitles(arrayTitle)
        //设置轮播间隔时间
        banner.setDelayTime(3000)
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true)
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
        banner.setOnClickListener()
        {
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",cartooningNovel2)
            startActivity(intent)
        }

        // 下标从0开始
        banner.setOnBannerListener()
        {
            if(it==0) {
                val intent =Intent(this,NovelDetailsActivity::class.java)
                intent.putExtra("nowNovelInfo",cartooningNovel2)
                startActivity(intent)
            }
            if(it==1) {
                val intent =Intent(this,NovelDetailsActivity::class.java)
                intent.putExtra("nowNovelInfo",cartooningNovel1)
                startActivity(intent)
            }
            if(it==2) {
                val intent =Intent(this,NovelDetailsActivity::class.java)
                intent.putExtra("nowNovelInfo",recentUpdateNovel2)
                startActivity(intent)
            }
            if(it==3) {
                val intent =Intent(this,NovelDetailsActivity::class.java)
                intent.putExtra("nowNovelInfo",recentUpdateNovel1)
                startActivity(intent)
            }
            if(it==4) {
                val intent =Intent(this,NovelDetailsActivity::class.java)
                intent.putExtra("nowNovelInfo",recentUpdateNovel3)
                startActivity(intent)
            }
        }
        banner.start()
    }
    //自定义的图片加载器
    private inner class MyLoader : ImageLoader() {
        override fun displayImage(context: Context, path: Any, imageView: ImageView) {
            Glide.with(context).load(path as String).into(imageView)
        }
    }
    private fun initViews() {
        // footer
        val btnFooterCartoons:ImageView =findViewById(R.id.iv_footer_cartoons)
        val btnFooterNews:ImageView =findViewById(R.id.iv_footer_news)
        val btnFooterNovels:ImageView =findViewById(R.id.iv_footer_novels)
        val btnFooterMine:ImageView =findViewById(R.id.iv_footer_mine)

        // xml 中默认所有字体图片为灰色
        // 设置页首当前界面字体颜色为蓝色
        // android:textColor="#00BFFF"
        //设置页首光标
//            TODO()
        // 设置页脚当前界面图片颜色为蓝色
        btnFooterNovels.setImageResource(R.drawable.footer_novels_blue)



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
            val intent =Intent(this,MineActivity::class.java)
            startActivity(intent)
        }
    }
    private fun novelCoverInit(){
//        最近更新
        ll_novel_recent_update1.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",recentUpdateNovel1)
            startActivity(intent)
        }
        ll_novel_recent_update2.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",recentUpdateNovel2)
            startActivity(intent)
        }
        ll_novel_recent_update3.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",recentUpdateNovel3)
            startActivity(intent)
        }
//        动画进行时
        ll_novel_cartooning1.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",cartooningNovel1)
            startActivity(intent)
        }
        ll_novel_cartooning2.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",cartooningNovel2)
            startActivity(intent)
        }
        ll_novel_cartooning3.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",cartooningNovel3)
            startActivity(intent)
        }
//        即将动漫画
        ll_novel_will_be_cartoonful1.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",willBeCartoonfulNovel1)
            startActivity(intent)
        }
        ll_novel_will_be_cartoonful2.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",willBeCartoonfulNovel2)
            startActivity(intent)
        }
        ll_novel_will_be_cartoonful3.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",willBeCartoonfulNovel3)
            startActivity(intent)
        }
//      经典必看
        ll_novel_class_must_look1.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",classMustLookNovel1)
            startActivity(intent)
        }
        ll_novel_class_must_look2.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",classMustLookNovel2)
            startActivity(intent)
        }
        ll_novel_class_must_look3.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",classMustLookNovel3)
            startActivity(intent)
        }
        ll_novel_class_must_look4.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",classMustLookNovel4)
            startActivity(intent)
        }
        ll_novel_class_must_look5.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",classMustLookNovel5)
            startActivity(intent)
        }
        ll_novel_class_must_look6.setOnClickListener(){
            val intent =Intent(this,NovelDetailsActivity::class.java)
            intent.putExtra("nowNovelInfo",classMustLookNovel6)
            startActivity(intent)
        }
    }
    private fun novelFunInit(){
        iv_novel_searchNovels.setOnClickListener(){}
        iv_novels_goNovels.setOnClickListener(){
            val intent =Intent(this,GoNovelActivity::class.java)
            startActivity(intent)
        }
        iv_novels_rankNovels.setOnClickListener(){
            val intent =Intent(this,NovelRankActivity::class.java)
            startActivity(intent)
        }
        iv_novel_header_search.setOnClickListener(){
            val intent =Intent(this,SearchActivity::class.java)
            intent.putExtra("searchType","NovelActivity")
            startActivity(intent)
        }
    }
}