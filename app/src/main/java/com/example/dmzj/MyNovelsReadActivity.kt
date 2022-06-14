package com.example.dmzj

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_my_novels_read.*
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.dialog_read_setting_footer.*
import kotlinx.android.synthetic.main.dialog_read_setting_header.*

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import android.provider.Settings.SettingNotFoundException





class MyNovelsReadActivity : AppCompatActivity() {
//    var novelStr: String = ""
    var IntentStr:String=""
    var nowChapter:Int=0
    lateinit var  nowNovelInfo:NovelInfo
//    获取章节的小说内容
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/novels/"+nowNovelInfo.novelURL+"/"+nowChapter+".txt"
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
            tv_read_novels.text = response
//            novelStr = response
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        nowNovelInfo  = intent.getSerializableExtra("nowNovelInfo") as NovelInfo
        nowChapter=intent.getIntExtra("nowChapter",1)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_my_novels_read)
        IntentStr = intent.getStringExtra("txtUrlStr").toString()

        sendRequestWithHttpURLConnection()
        initViews()
    }

    fun initViews() {
//        控件的声明
        val settingHeader = findViewById<View>(R.id.id_dialog_read_setting_header)
        val settingFooter = findViewById<View>(R.id.id_dialog_read_setting_footer)
//        val settingDetail = findViewById<View>(R.id.id_dialog_read_setting_detail)

//
        val ll_setting = findViewById<LinearLayout>(R.id.ll_setting)
        val iv_setting = findViewById<ImageView>(R.id.iv_setting)
        val iv_title_back = findViewById<ImageView>(R.id.iv_title_back)
        val ll_chapter_list=findViewById<LinearLayout>(R.id.ll_chapter_list)

        val rl_title_view=findViewById<RelativeLayout>(R.id.rl_title_view)


//
        tv_book_name.text=nowNovelInfo.novelName


//        --------------------------------------------------------------------------------------------底边按钮的声明
        val ll_bottom_view=findViewById<LinearLayout>(R.id.ll_bottom_view)
        val ll_night_and_day=findViewById<LinearLayout>(R.id.ll_night_and_day)
        val tv_night_and_day=findViewById<TextView>(R.id.tv_night_and_day)
        var iv_night_and_day=findViewById<ImageView>(R.id.iv_night_and_day)

//        --------------------------------------------------------------------------------------------详细设置按钮的声明
        val ll_bottom_view_detail=findViewById<LinearLayout>(R.id.ll_bottom_view_detail)

//        修改字号
        val tv_reduce_text_size=findViewById<TextView>(R.id.tv_reduce_text_size)
        val tv_increase_text_size=findViewById<TextView>(R.id.tv_increase_text_size)
        var tv_text_size =findViewById<TextView>(R.id.tv_text_size)
//      上下章按钮
        val tv_last_chapter=findViewById<TextView>(R.id.tv_last_chapter)
        val tv_next_chapter=findViewById<TextView>(R.id.tv_next_chapter)

        //阅读背景风格
        val ivCommonStyle = findViewById<ImageView>(R.id.iv_common_style)
        val ivLeatherStyle = findViewById<ImageView>(R.id.iv_leather_style)
        val ivProtectEyeStyle = findViewById<ImageView>(R.id.iv_protect_eye_style)
        val ivBreenStyle = findViewById<ImageView>(R.id.iv_breen_style)
        val ivBlueDeepStyle = findViewById<ImageView>(R.id.iv_blue_deep_style)
//      亮度跟随系统
        val tv_system_brightness=findViewById<TextView>(R.id.tv_system_brightness)


//        显示隐藏按钮的显示属性
        tv_read_novels.setOnClickListener() {
            if(id_dialog_read_setting_header.isVisible==true)
            {
                id_dialog_read_setting_header.isVisible =false
                id_dialog_read_setting_footer.isVisible =false
                ll_bottom_view_detail.isVisible =false
                ll_bottom_view.isVisible =false

            }
            else
            {
                id_dialog_read_setting_header.isVisible =true
                id_dialog_read_setting_footer.isVisible =true
                ll_bottom_view.isVisible =true
            }
        }
//        -------------------------------------------------------------------------------------------标题栏返回
        iv_title_back.setOnClickListener() {
            finish()
        }
//        -------------------------------------------------------------------------------------------底边按钮


//       底边设置
        ll_setting.setOnClickListener() {
            ll_bottom_view.isVisible =false
            ll_bottom_view_detail.isVisible =true
        }

        ll_night_and_day.setOnClickListener()
        {
            if(tv_night_and_day.text.toString()=="夜间")
            {
                tv_read_novels.setBackgroundColor(Color.rgb(0,0,0))
                tv_read_novels.setTextColor(Color.rgb(255,255,255))
                tv_night_and_day.text="日间"
                iv_night_and_day.setImageResource(R.mipmap.r4)
            }
            else{
                tv_read_novels.setBackgroundColor(Color.rgb(255,255,255))
                tv_read_novels.setTextColor(Color.rgb(0,0,0))
                tv_night_and_day.text="夜间"
                iv_night_and_day.setImageResource(R.mipmap.ao)

            }

        }
//        底边章节
        ll_chapter_list.setOnClickListener(){
            var intent = Intent(this, NovelsContentsActivity::class.java)
            intent.putExtra("nowNovelInfo",nowNovelInfo)
            intent.putExtra("nowChapter",nowChapter)

            startActivity(intent)
        }
//      上一章下一章
        tv_last_chapter.setOnClickListener() {
            if(nowChapter!=1) {
                val intent = Intent(this, MyNovelsReadActivity::class.java)
                intent.putExtra("nowNovelInfo", nowNovelInfo)
                intent.putExtra("nowChapter", nowChapter - 1)
                startActivity(intent)
            }
            else {
                Toast.makeText(this,"已经是第一章",Toast.LENGTH_LONG).show()
            }
        }
        tv_next_chapter.setOnClickListener() {
            if (nowChapter != nowNovelInfo.novelChapterNum) {
                val intent = Intent(this, MyNovelsReadActivity::class.java)
                intent.putExtra("nowNovelInfo", nowNovelInfo)
                intent.putExtra("nowChapter", nowChapter + 1)
                startActivity(intent)
            } else {
                Toast.makeText(this, "已经是最后一章", Toast.LENGTH_LONG).show()
            }
        }


//        -------------------------------------------------------------------------------------------详细设置

//        修改字号
        tv_reduce_text_size.setOnClickListener(){
            tv_text_size.text  = (tv_text_size.text.toString().toInt()-1).toString()
            tv_read_novels.setTextSize(TypedValue.COMPLEX_UNIT_DIP,tv_text_size.text.toString().toFloat())
        }
        tv_increase_text_size.setOnClickListener(){
            tv_text_size.text  = (tv_text_size.text.toString().toInt()+1).toString()
            tv_read_novels.setTextSize(TypedValue.COMPLEX_UNIT_DIP,tv_text_size.text.toString().toFloat())
        }

//        修改阅读背景颜色
//        白色
        ivCommonStyle.setOnClickListener() {
            ivCommonStyle.isSelected = false
            ivLeatherStyle.isSelected = false
            ivProtectEyeStyle.isSelected = false
            ivBreenStyle.isSelected = false
            ivBlueDeepStyle.isSelected = false
            ivCommonStyle.isSelected = true
            tv_read_novels.setBackgroundColor(Color.rgb(245,244,240))
            tv_read_novels.setTextColor(Color.rgb(0,0,0))
        }
        ivLeatherStyle.setOnClickListener()
        {
            ivCommonStyle.isSelected = false
            ivLeatherStyle.isSelected = false
            ivProtectEyeStyle.isSelected = false
            ivBreenStyle.isSelected = false
            ivBlueDeepStyle.isSelected = false
            ivLeatherStyle.isSelected = true
            tv_read_novels.setBackgroundColor(Color.rgb(230,219,191))
            tv_read_novels.setTextColor(Color.rgb(0,0,0))
        }
        ivProtectEyeStyle.setOnClickListener()
        {
            ivCommonStyle.isSelected = false
            ivLeatherStyle.isSelected = false
            ivProtectEyeStyle.isSelected = false
            ivBreenStyle.isSelected = false
            ivBlueDeepStyle.isSelected = false
            ivProtectEyeStyle.isSelected = true

            tv_read_novels.setBackgroundColor(Color.rgb(206,235,205))
            tv_read_novels.setTextColor(Color.rgb(0,0,0))
        }
        ivBreenStyle.setOnClickListener()
        {
            ivCommonStyle.isSelected = false
            ivLeatherStyle.isSelected = false
            ivProtectEyeStyle.isSelected = false
            ivBreenStyle.isSelected = false
            ivBlueDeepStyle.isSelected = false
            ivBreenStyle.isSelected = true
            tv_read_novels.setBackgroundColor(Color.rgb(182,189,155))
            tv_read_novels.setTextColor(Color.rgb(0,0,0))
        }
        ivBlueDeepStyle.setOnClickListener()
        {
            ivCommonStyle.isSelected = false
            ivLeatherStyle.isSelected = false
            ivProtectEyeStyle.isSelected = false
            ivBreenStyle.isSelected = false
            ivBlueDeepStyle.isSelected = false
            ivBlueDeepStyle.isSelected = true
            tv_read_novels.setBackgroundColor(Color.rgb(0,28,40))
            tv_read_novels.setTextColor(Color.rgb(255,255,255))
        }


//        亮度跟随系统
//

        //亮度调节
        val seekBar = findViewById<SeekBar>(R.id.sb_brightness_progress)
        val tvBrightFollowSystem = findViewById<View>(R.id.tv_system_brightness)
//        seekBar.progress = setting.getBrightProgress()
//        tvBrightFollowSystem.isSelected = setting.isBrightFollowSystem()
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                this@MyNovelsReadActivity.setScreenBrightness(seekBar.progress.toFloat() / 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

//        亮度跟随系统
        tv_system_brightness.setOnClickListener(){
            tv_system_brightness.isSelected = !tv_system_brightness.isSelected
            if (tv_system_brightness.isSelected()) {
                setScreenBrightness(getSystemScreenBrightness(this@MyNovelsReadActivity).toFloat())
            }
            else{
//                设置为当面Seekbar的亮度
                this@MyNovelsReadActivity.setScreenBrightness(seekBar.progress.toFloat() / 100)
            }
        }

    }
    private fun setScreenBrightness(num: Float) {
        //取得屏幕的属性
        val layoutParams = super.getWindow().attributes
        //设置屏幕的亮度
        layoutParams.screenBrightness = num
        //重新设置窗口的属性
        super.getWindow().attributes = layoutParams
    }
    fun getSystemScreenBrightness(activity: Activity): Int {
        try {
            return Settings.System.getInt(
                activity.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS
            )
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }
}