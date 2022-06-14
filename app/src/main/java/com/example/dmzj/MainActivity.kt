package com.example.dmzj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
//import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.banner
import kotlinx.android.synthetic.main.header_layout_cartoons.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    lateinit var recentMustLookCartoon1:CartoonsInfo
    lateinit var recentMustLookCartoon2:CartoonsInfo
    lateinit var recentMustLookCartoon3:CartoonsInfo
    lateinit var randomLookCartoon1:CartoonsInfo
    lateinit var randomLookCartoon2:CartoonsInfo
    lateinit var randomLookCartoon3:CartoonsInfo
    lateinit var chinaCartoon1:CartoonsInfo
    lateinit var chinaCartoon2:CartoonsInfo
    lateinit var chinaCartoon3:CartoonsInfo
    lateinit var chinaCartoon4:CartoonsInfo
    lateinit var chinaCartoon5:CartoonsInfo
    lateinit var chinaCartoon6:CartoonsInfo
    lateinit var americanCartoon1:CartoonsInfo
    lateinit var americanCartoon2:CartoonsInfo
    lateinit var americanCartoon3:CartoonsInfo
    lateinit var americanCartoon4:CartoonsInfo
    lateinit var  hotContinueCartoon1:CartoonsInfo
    lateinit var  hotContinueCartoon2:CartoonsInfo
    lateinit var  hotContinueCartoon3:CartoonsInfo
    lateinit var  hotContinueCartoon4:CartoonsInfo
    lateinit var  hotContinueCartoon5:CartoonsInfo
    lateinit var  hotContinueCartoon6:CartoonsInfo
    lateinit var tiaomanCartoon1:CartoonsInfo
    lateinit var tiaomanCartoon2:CartoonsInfo
    lateinit var tiaomanCartoon3:CartoonsInfo
    lateinit var tiaomanCartoon4:CartoonsInfo
    lateinit var  newRackingCartoon1:CartoonsInfo
    lateinit var  newRackingCartoon2:CartoonsInfo
    lateinit var  newRackingCartoon3:CartoonsInfo
    lateinit var  newRackingCartoon4:CartoonsInfo
    lateinit var  newRackingCartoon5:CartoonsInfo
    lateinit var  newRackingCartoon6:CartoonsInfo
    val newRackingList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
    private lateinit var pull_refresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
//        var uer: GlobalUser = application as GlobalUser
//        uer.userName="马猴烧酒薇薇"
//        uer.userHeadURL="shaojiuweiwei"
//        uer.userBirthday="2000-11-06"
//        uer.userPassword="wwwwww"
//        uer.userConstellation=" "
//        uer.userSex=0
//        uer.userID=1
//        uer.userLevel=6
        recentMustLookSendRequestWithHttpURLConnection()
        ChinaCartoonSendRequestWithHttpURLConnection()
//        AmericanCartoonSendRequestWithHttpURLConnection()
        RandomLookSendRequestWithHttpURLConnection()
        hotContinueSendRequestWithHttpURLConnection()
        newRackingSendRequestWithHttpURLConnection()
        super.onCreate(savedInstanceState)
        // 隐藏标题栏
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        initViews()
        bannerInit()
        cartoonCoverInit()
        pullRefreshInit()
    }
    private fun pullRefreshInit(){
        pull_refresh = findViewById<SwipeRefreshLayout>(R.id.pull_refresh)
        pull_refresh.setColorSchemeColors(Color.parseColor("#00BFFF"))
        pull_refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                runOnUiThread{
                    bannerInit()
                    recentMustLookSendRequestWithHttpURLConnection()
                    ChinaCartoonSendRequestWithHttpURLConnection()
                    //        AmericanCartoonSendRequestWithHttpURLConnection()
                    RandomLookSendRequestWithHttpURLConnection()
                    hotContinueSendRequestWithHttpURLConnection()
                    newRackingSendRequestWithHttpURLConnection()
                    pull_refresh.isRefreshing = false
                }
            }
        }
    }
    private fun recentMustLookSendRequestWithHttpURLConnection() {
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
                recentMustLookRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun recentMustLookRefresh(jsonData:String){
        try {
            val data=ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
            tempList.clear()
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
                tempList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
                ))
            }
            var isRandomed=ArrayList<Boolean>()
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            recentMustLookCartoon1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            Log.d("tag","打印信息：${recentMustLookCartoon1}")
            var randomNum2=random.nextInt(tempList.size)
            recentMustLookCartoon2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            Log.d("tag","打印信息：${recentMustLookCartoon2}")
            var randomNum3=random.nextInt(tempList.size)
            recentMustLookCartoon3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            Log.d("tag","打印信息：${recentMustLookCartoon3}")
            runOnUiThread(){
                tv_must_look1_name.text=recentMustLookCartoon1.cartoonName
                tv_must_look2_name.text=recentMustLookCartoon2.cartoonName
                tv_must_look3_name.text=recentMustLookCartoon3.cartoonName
                tv_must_look1_author.text=recentMustLookCartoon1.cartoonAuthor
                tv_must_look2_author.text=recentMustLookCartoon2.cartoonAuthor
                tv_must_look3_author.text=recentMustLookCartoon3.cartoonAuthor

                val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+recentMustLookCartoon1.cartoonCoverURL+".png"
                val image :ImageView= iv_must_look1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)


                val imgUrl2 = "http://192.168.137.1:8080/cartoon_cover/"+recentMustLookCartoon2.cartoonCoverURL+".png"
                val image2 :ImageView= iv_must_look2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/cartoon_cover/"+recentMustLookCartoon3.cartoonCoverURL+".png"
                val image3 :ImageView= iv_must_look3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }

    }
    private fun RandomLookSendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getCartoonRecommendRandomLook.jsp"
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
                RandomLookRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun RandomLookRefresh(jsonData:String){
        try {
            val data=ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
            tempList.clear()
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
                tempList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
                ))
            }
            var isRandomed=ArrayList<Boolean>()
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            randomLookCartoon1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            Log.d("tag","打印信息：${randomLookCartoon1}")
            var randomNum2=random.nextInt(tempList.size)
            randomLookCartoon2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            Log.d("tag","打印信息：${randomLookCartoon2}")
            var randomNum3=random.nextInt(tempList.size)
            randomLookCartoon3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            Log.d("tag","打印信息：${randomLookCartoon3}")
            runOnUiThread(){
                tv_random_look1_name.text=randomLookCartoon1.cartoonName
                tv_random_look2_name.text=randomLookCartoon2.cartoonName
                tv_random_look3_name.text=randomLookCartoon3.cartoonName
                tv_random_look1_author.text=randomLookCartoon1.cartoonAuthor
                tv_random_look2_author.text=randomLookCartoon2.cartoonAuthor
                tv_random_look3_author.text=randomLookCartoon3.cartoonAuthor

                val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+randomLookCartoon1.cartoonCoverURL+".png"
                val image :ImageView= iv_random_look1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)
                iv_random_look1.maxHeight=180
                iv_random_look1.maxWidth=320

                val imgUrl2 = "http://192.168.137.1:8080/cartoon_cover/"+randomLookCartoon2.cartoonCoverURL+".png"
                val image2 :ImageView= iv_random_look2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/cartoon_cover/"+randomLookCartoon3.cartoonCoverURL+".png"
                val image3 :ImageView= iv_random_look3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    private fun ChinaCartoonSendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getCartoonRecommendChinaCartoon.jsp"
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
                ChinaCartoonRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun ChinaCartoonRefresh(jsonData:String){
        try {
            val data=ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
            tempList.clear()
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
                tempList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
                ))
            }
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            chinaCartoon1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            var randomNum2=random.nextInt(tempList.size)
            chinaCartoon2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            var randomNum3=random.nextInt(tempList.size)
            chinaCartoon3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            var randomNum4=random.nextInt(tempList.size)
            chinaCartoon4=tempList[randomNum4]
            tempList.removeAt(randomNum4)
            var randomNum5=random.nextInt(tempList.size)
            chinaCartoon5=tempList[randomNum5]
            tempList.removeAt(randomNum5)
            var randomNum6=random.nextInt(tempList.size)
            chinaCartoon6=tempList[randomNum6]
            tempList.removeAt(randomNum6)
            runOnUiThread(){
                tv_china_cartoon1_name.text=chinaCartoon1.cartoonName
                tv_china_cartoon2_name.text=chinaCartoon2.cartoonName
                tv_china_cartoon3_name.text=chinaCartoon3.cartoonName
                tv_china_cartoon4_name.text=chinaCartoon4.cartoonName
                tv_china_cartoon5_name.text=chinaCartoon5.cartoonName
                tv_china_cartoon6_name.text=chinaCartoon6.cartoonName
                tv_china_cartoon1_author.text=chinaCartoon1.cartoonAuthor
                tv_china_cartoon2_author.text=chinaCartoon2.cartoonAuthor
                tv_china_cartoon3_author.text=chinaCartoon3.cartoonAuthor
                tv_china_cartoon4_author.text=chinaCartoon4.cartoonAuthor
                tv_china_cartoon5_author.text=chinaCartoon5.cartoonAuthor
                tv_china_cartoon6_author.text=chinaCartoon6.cartoonAuthor
                val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+chinaCartoon1.cartoonCoverURL+".png"
                val image :ImageView= iv_china_cartoon1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)

                val imgUrl2 = "http://192.168.137.1:8080/cartoon_cover/"+chinaCartoon2.cartoonCoverURL+".png"
                val image2 :ImageView= iv_china_cartoon2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/cartoon_cover/"+chinaCartoon3.cartoonCoverURL+".png"
                val image3 :ImageView= iv_china_cartoon3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

                val imgUrl4 = "http://192.168.137.1:8080/cartoon_cover/"+chinaCartoon4.cartoonCoverURL+".png"
                val image4 :ImageView= iv_china_cartoon4
                val Context4: Context = image.context
                Glide.with(Context4).load(imgUrl4).dontAnimate().into(image4)

                val imgUrl5 = "http://192.168.137.1:8080/cartoon_cover/"+chinaCartoon5.cartoonCoverURL+".png"
                val image5 :ImageView= iv_china_cartoon5
                val Context5: Context = image.context
                Glide.with(Context5).load(imgUrl5).dontAnimate().into(image5)

                val imgUrl6 = "http://192.168.137.1:8080/cartoon_cover/"+chinaCartoon6.cartoonCoverURL+".png"
                val image6 :ImageView= iv_china_cartoon6
                val Context6: Context = image.context
                Glide.with(Context6).load(imgUrl6).dontAnimate().into(image6)

            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
//    private fun AmericanCartoonSendRequestWithHttpURLConnection() {
//        thread {
//            var connection: HttpURLConnection? = null
//            try {
//                val response = StringBuffer()
//                val urlStr:String="http://192.168.137.1:8080/getCartoonRecommendAmericanCartoon.jsp"
//                val url = URL(urlStr)
//                connection = url.openConnection() as HttpURLConnection
//                connection.connectTimeout = 8000
//                connection.readTimeout = 8000
//                val input = connection.inputStream
//                val reader = BufferedReader(InputStreamReader(input))
//                reader.use {
//                    reader.forEachLine {
//                        response.append(it)
//                    }
//                }
//                AmericanCartoonRefresh(response.toString())
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                connection?.disconnect()
//            }
//        }
//    }
//    private fun AmericanCartoonRefresh(jsonData:String){
//        try {
//            val data=ArrayList<CartoonsInfo>()
//            //json数组
//            val jsonArray= JSONArray(jsonData)
//            val tempList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
//            tempList.clear()
//            for(i in 0 until jsonArray.length()){
//                val jsonObject=jsonArray.getJSONObject(i)
//
//                val cartoonName=jsonObject.getString("cartoonName")
//                val cartoonDescription=jsonObject.getString("cartoonDescription")
//                val cartoonAuthor=jsonObject.getString("cartoonAuthor")
//                val cartoonURL=jsonObject.getString("cartoonURL")
//                val cartoonCoverURL=jsonObject.getString("cartoonCoverURL")
//                val cartoonLastUpdateTime=jsonObject.getString("cartoonLastUpdateTime")
//                val cartoonType=jsonObject.getString("cartoonType")
//                val cartoonID:Int=jsonObject.getString("cartoonID").toInt()
//                val cartoonChapterNum:Int=jsonObject.getString("cartoonChapterNum").toInt()
//                val cartoonHot:Int=jsonObject.getString("cartoonHot").toInt()
//                val cartoonSubscriptionsNum:Int=jsonObject.getString("cartoonSubscriptionsNum").toInt()
//                val cartoonCommentNum:Int=jsonObject.getString("cartoonCommentNum").toInt()
//                val cartoonIsFinished:Int=jsonObject.getString("cartoonIsFinished").toInt()
//                tempList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
//                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
//                ))
//            }
//            var random = Random() //指定种子数字
//            var randomNum1=random.nextInt(tempList.size)
//            americanCartoon1=tempList[randomNum1]
//            tempList.removeAt(randomNum1)
//            var randomNum2=random.nextInt(tempList.size)
//            americanCartoon2=tempList[randomNum2]
//            tempList.removeAt(randomNum2)
//            var randomNum3=random.nextInt(tempList.size)
//            americanCartoon3=tempList[randomNum3]
//            tempList.removeAt(randomNum3)
//            var randomNum4=random.nextInt(tempList.size)
//            americanCartoon4=tempList[randomNum4]
//            tempList.removeAt(randomNum4)
//            runOnUiThread(){
//                tv_american_cartoon1_name.text=americanCartoon1.cartoonName
//                tv_american_cartoon2_name.text=americanCartoon2.cartoonName
//                tv_american_cartoon3_name.text=americanCartoon3.cartoonName
//                tv_american_cartoon4_name.text=americanCartoon4.cartoonName
//
//                val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+americanCartoon1.cartoonCoverURL+".png"
//                val image :ImageView= iv_american_cartoon1
//                val Context: Context = image.context
//                Glide.with(Context).load(imgUrl).dontAnimate().into(image)
//
//                val imgUrl2 = "http://192.168.137.1:8080/cartoon_cover/"+americanCartoon2.cartoonCoverURL+".png"
//                val image2 :ImageView= iv_american_cartoon2
//                val Context2: Context = image.context
//                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)
//
//                val imgUrl3 = "http://192.168.137.1:8080/cartoon_cover/"+americanCartoon3.cartoonCoverURL+".png"
//                val image3 :ImageView= iv_american_cartoon3
//                val Context3: Context = image.context
//                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)
//
//                val imgUrl4 = "http://192.168.137.1:8080/cartoon_cover/"+americanCartoon4.cartoonCoverURL+".png"
//                val image4 :ImageView= iv_american_cartoon4
//                val Context4: Context = image.context
//                Glide.with(Context4).load(imgUrl4).dontAnimate().into(image4)
//
//
//            }
//        }catch (e: Exception){
//            e.printStackTrace()
//        }
//
//    }
    private fun hotContinueSendRequestWithHttpURLConnection() {
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
                hotContinueRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun hotContinueRefresh(jsonData:String){
        try {
            val data=ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
            tempList.clear()
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
                tempList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
                ))
            }
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            hotContinueCartoon1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            var randomNum2=random.nextInt(tempList.size)
            hotContinueCartoon2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            var randomNum3=random.nextInt(tempList.size)
            hotContinueCartoon3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            var randomNum4=random.nextInt(tempList.size)
            hotContinueCartoon4=tempList[randomNum4]
            tempList.removeAt(randomNum4)
            var randomNum5=random.nextInt(tempList.size)
            hotContinueCartoon5=tempList[randomNum5]
            tempList.removeAt(randomNum5)
            var randomNum6=random.nextInt(tempList.size)
            hotContinueCartoon6=tempList[randomNum6]
            tempList.removeAt(randomNum6)
            runOnUiThread(){
                tv_hot_continue1_name.text=hotContinueCartoon1.cartoonName
                tv_hot_continue2_name.text=hotContinueCartoon2.cartoonName
                tv_hot_continue3_name.text=hotContinueCartoon3.cartoonName
                tv_hot_continue4_name.text=hotContinueCartoon4.cartoonName
                tv_hot_continue5_name.text=hotContinueCartoon5.cartoonName
                tv_hot_continue6_name.text=hotContinueCartoon6.cartoonName
                tv_hot_continue1_author.text=hotContinueCartoon1.cartoonAuthor
                tv_hot_continue2_author.text=hotContinueCartoon2.cartoonAuthor
                tv_hot_continue3_author.text=hotContinueCartoon3.cartoonAuthor
                tv_hot_continue4_author.text=hotContinueCartoon4.cartoonAuthor
                tv_hot_continue5_author.text=hotContinueCartoon5.cartoonAuthor
                tv_hot_continue6_author.text=hotContinueCartoon6.cartoonAuthor
                val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+hotContinueCartoon1.cartoonCoverURL+".png"
                val image :ImageView= iv_hot_continue1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)

                val imgUrl2 = "http://192.168.137.1:8080/cartoon_cover/"+hotContinueCartoon2.cartoonCoverURL+".png"
                val image2 :ImageView= iv_hot_continue2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/cartoon_cover/"+hotContinueCartoon3.cartoonCoverURL+".png"
                val image3 :ImageView= iv_hot_continue3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

                val imgUrl4 = "http://192.168.137.1:8080/cartoon_cover/"+hotContinueCartoon4.cartoonCoverURL+".png"
                val image4 :ImageView= iv_hot_continue4
                val Context4: Context = image.context
                Glide.with(Context4).load(imgUrl4).dontAnimate().into(image4)

                val imgUrl5 = "http://192.168.137.1:8080/cartoon_cover/"+hotContinueCartoon5.cartoonCoverURL+".png"
                val image5 :ImageView= iv_hot_continue5
                val Context5: Context = image.context
                Glide.with(Context5).load(imgUrl5).dontAnimate().into(image5)

                val imgUrl6 = "http://192.168.137.1:8080/cartoon_cover/"+hotContinueCartoon6.cartoonCoverURL+".png"
                val image6 :ImageView= iv_hot_continue6
                val Context6: Context = image.context
                Glide.with(Context6).load(imgUrl6).dontAnimate().into(image6)

            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    private fun newRackingSendRequestWithHttpURLConnection() {
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
                newRackingRefresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun newRackingRefresh(jsonData:String){
        try {
            val data=ArrayList<CartoonsInfo>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            val tempList: ArrayList<CartoonsInfo> =ArrayList<CartoonsInfo>()
            tempList.clear()
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
                tempList.add(CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                    cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished
                ))
            }
            var random = Random() //指定种子数字
            var randomNum1=random.nextInt(tempList.size)
            newRackingCartoon1=tempList[randomNum1]
            tempList.removeAt(randomNum1)
            var randomNum2=random.nextInt(tempList.size)
            newRackingCartoon2=tempList[randomNum2]
            tempList.removeAt(randomNum2)
            var randomNum3=random.nextInt(tempList.size)
            newRackingCartoon3=tempList[randomNum3]
            tempList.removeAt(randomNum3)
            var randomNum4=random.nextInt(tempList.size)
            newRackingCartoon4=tempList[randomNum4]
            tempList.removeAt(randomNum4)
            var randomNum5=random.nextInt(tempList.size)
            newRackingCartoon5=tempList[randomNum5]
            tempList.removeAt(randomNum5)
            var randomNum6=random.nextInt(tempList.size)
            newRackingCartoon6=tempList[randomNum6]
            tempList.removeAt(randomNum6)
            runOnUiThread(){
                tv_new_racking1_name.text=newRackingCartoon1.cartoonName
                tv_new_racking2_name.text=newRackingCartoon2.cartoonName
                tv_new_racking3_name.text=newRackingCartoon3.cartoonName
                tv_new_racking4_name.text=newRackingCartoon4.cartoonName
                tv_new_racking5_name.text=newRackingCartoon5.cartoonName
                tv_new_racking6_name.text=newRackingCartoon6.cartoonName
                tv_new_racking1_author.text=newRackingCartoon1.cartoonAuthor
                tv_new_racking2_author.text=newRackingCartoon2.cartoonAuthor
                tv_new_racking3_author.text=newRackingCartoon3.cartoonAuthor
                tv_new_racking4_author.text=newRackingCartoon4.cartoonAuthor
                tv_new_racking5_author.text=newRackingCartoon5.cartoonAuthor
                tv_new_racking6_author.text=newRackingCartoon6.cartoonAuthor
                val imgUrl = "http://192.168.137.1:8080/cartoon_cover/"+newRackingCartoon1.cartoonCoverURL+".png"
                val image :ImageView= iv_new_racking1
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)

                val imgUrl2 = "http://192.168.137.1:8080/cartoon_cover/"+newRackingCartoon2.cartoonCoverURL+".png"
                val image2 :ImageView= iv_new_racking2
                val Context2: Context = image.context
                Glide.with(Context2).load(imgUrl2).dontAnimate().into(image2)

                val imgUrl3 = "http://192.168.137.1:8080/cartoon_cover/"+newRackingCartoon3.cartoonCoverURL+".png"
                val image3 :ImageView= iv_new_racking3
                val Context3: Context = image.context
                Glide.with(Context3).load(imgUrl3).dontAnimate().into(image3)

                val imgUrl4 = "http://192.168.137.1:8080/cartoon_cover/"+newRackingCartoon4.cartoonCoverURL+".png"
                val image4 :ImageView= iv_new_racking4
                val Context4: Context = image.context
                Glide.with(Context4).load(imgUrl4).dontAnimate().into(image4)

                val imgUrl5 = "http://192.168.137.1:8080/cartoon_cover/"+newRackingCartoon5.cartoonCoverURL+".png"
                val image5 :ImageView= iv_new_racking5
                val Context5: Context = image.context
                Glide.with(Context5).load(imgUrl5).dontAnimate().into(image5)

                val imgUrl6 = "http://192.168.137.1:8080/cartoon_cover/"+newRackingCartoon6.cartoonCoverURL+".png"
                val image6 :ImageView= iv_new_racking6
                val Context6: Context = image.context
                Glide.with(Context6).load(imgUrl6).dontAnimate().into(image6)

            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    private fun bannerInit(){
        val arrayImageUrl = arrayListOf<String>("http://192.168.137.1:8080/banner_cover/9.png",
            "http://192.168.137.1:8080/banner_cover/8.png",
            "http://192.168.137.1:8080/banner_cover/6.png",
            "http://192.168.137.1:8080/banner_cover/5.png",
            "http://192.168.137.1:8080/banner_cover/9.png")
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
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", recentMustLookCartoon2)
            startActivity(intent)
        }

        // 下标从0开始
        banner.setOnBannerListener()
        {
            if(it==0) {
                val intent =Intent(this,CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", recentMustLookCartoon1)
                startActivity(intent)
            }
            if(it==1) {
                val intent =Intent(this,CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", recentMustLookCartoon2)
                startActivity(intent)
            }
            if(it==2) {
                val intent =Intent(this,CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", recentMustLookCartoon3)
                startActivity(intent)
            }
            if(it==3) {
                val intent =Intent(this,CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", hotContinueCartoon1)
                startActivity(intent)
            }
            if(it==4) {
                val intent =Intent(this,CartoonsDetailsActivity::class.java)
                intent.putExtra("nowCartoonsInfo", hotContinueCartoon2)
                startActivity(intent)
            }
        }
        //        banner.setOnBannerListener {
//            Log.d("=*=", "第几张" + it.dec())
//        }
        //必须最后调用的方法，启动轮播图。
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
        // header
        val btnHeaderRecommend:TextView =findViewById(R.id.tv_header_recommend)
        val btnHeaderUpdate:TextView =findViewById(R.id.tv_update)
        val btnHeaderClassification:TextView =findViewById(R.id.tv_header_classification)
        val btnHeaderRank:TextView =findViewById(R.id.tv_header_rank)
//        val btnHeaderWelfare:TextView =findViewById(R.id.tv_header_welfare)
        // xml 中默认所有字体图片为灰色
        // 设置页首当前界面字体颜色为蓝色
        // android:textColor="#00BFFF"
        btnHeaderRecommend.setTextColor(Color.rgb(0,191,255))
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
            intent.putExtra("searchType","MainActivity")
            startActivity(intent)
        }
    }
    private fun cartoonCoverInit(){
        //        刷新内容
        iv_refresh_random_look.setOnClickListener()
        {
            RandomLookSendRequestWithHttpURLConnection()
        }
        iv_refresh_china_cartoon.setOnClickListener()
        {
            ChinaCartoonSendRequestWithHttpURLConnection()
        }
        iv_refresh_hot_continue.setOnClickListener()
        {
            hotContinueSendRequestWithHttpURLConnection()
        }

//        必看
        iv_must_look1.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", recentMustLookCartoon1)
            startActivity(intent)
        }
        iv_must_look2.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", recentMustLookCartoon2)
            startActivity(intent)
        }
        iv_must_look3.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", recentMustLookCartoon3)
            startActivity(intent)
        }



//        随便看看
        ll_random_look1.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", randomLookCartoon1)
            startActivity(intent)
        }
        ll_random_look2.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", randomLookCartoon2)
            startActivity(intent)
        }
        ll_random_look3.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", randomLookCartoon3)
            startActivity(intent)
        }
//        国漫大事件
        ll_china_cartoon1.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", chinaCartoon1)
            startActivity(intent)
        }
        ll_china_cartoon2.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", chinaCartoon2)
            startActivity(intent)
        }
        ll_china_cartoon3.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", chinaCartoon3)
            startActivity(intent)
        }
        ll_china_cartoon4.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", chinaCartoon4)
            startActivity(intent)
        }
        ll_china_cartoon5.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", chinaCartoon5)
            startActivity(intent)
        }
        ll_china_cartoon6.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", chinaCartoon6)
            startActivity(intent)
        }


//      热门连载
        ll_hot_continue1.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", hotContinueCartoon1)
            startActivity(intent)
        }
        ll_hot_continue2.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", hotContinueCartoon2)
            startActivity(intent)
        }
        ll_hot_continue3.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", hotContinueCartoon3)
            startActivity(intent)
        }
        ll_hot_continue4.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", hotContinueCartoon4)
            startActivity(intent)
        }
        ll_hot_continue5.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", hotContinueCartoon5)
            startActivity(intent)
        }
        ll_hot_continue6.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", hotContinueCartoon6)
            startActivity(intent)
        }

//      最新上架
        ll_new_racking1.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", newRackingCartoon1)
            startActivity(intent)
        }
        ll_new_racking2.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", newRackingCartoon2)
            startActivity(intent)
        }
        ll_new_racking3.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", newRackingCartoon3)
            startActivity(intent)
        }
        ll_new_racking4.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", newRackingCartoon4)
            startActivity(intent)
        }
        ll_new_racking5.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", newRackingCartoon5)
            startActivity(intent)
        }
        ll_new_racking6.setOnClickListener()
        {
            val intent =Intent(this,CartoonsDetailsActivity::class.java)
            intent.putExtra("nowCartoonsInfo", newRackingCartoon6)
            startActivity(intent)
        }
    }
}
