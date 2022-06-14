package com.example.dmzj

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.core.view.isVisible

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import com.bumptech.glide.Glide
import com.google.android.material.internal.ContextUtils.getActivity

import kotlinx.android.synthetic.main.activity_cartoon_read.*
import kotlinx.android.synthetic.main.activity_cartoon_read.view.*
import kotlinx.android.synthetic.main.cartooons_read_footer_layout.*
import kotlinx.android.synthetic.main.cartooons_read_footer_layout.view.*
import kotlinx.android.synthetic.main.dialog_read_setting_header.view.*
import kotlinx.android.synthetic.main.page_cartoon_layout.view.*
import java.text.SimpleDateFormat
import java.time.*
import java.util.*
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cartoons_rank.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class CartoonReadActivity: Activity() {
    var nowChapter:Int=0
    var nowChapterPageNum:Int=0
    lateinit var  nowCartoonsInfo:CartoonsInfo
    var leftOrRightHand="Right"

    override fun onCreate(savedInstanceState: Bundle?) {
        nowCartoonsInfo  = intent.getSerializableExtra("nowCartoonsInfo") as CartoonsInfo
        nowChapterPageNum=intent.getIntExtra("nowChapterPageNum",1)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartoon_read)
        nowChapter=intent.getIntExtra("nowChapter",1)
        initViews()
        var id_cartoons_read_header=findViewById<View>(R.id.id_cartoons_read_header)
        var id_cartoons_read_footer=findViewById<View>(R.id.id_cartoons_read_footer)
        var vp:ViewPager2=findViewById(R.id.vp)
        vp.adapter = ViewData.RecyclerViewAdapter(this,vp,nowCartoonsInfo,nowChapter,nowChapterPageNum,id_cartoons_read_header, id_cartoons_read_footer,sb_read_cartoon_progress,leftOrRightHand,0)
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){})
        CartoonFooterInits()

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
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }
    fun CartoonFooterInits(){
        //        漫画名
        id_cartoons_read_header.tv_book_name.text=nowCartoonsInfo.cartoonName
//                默认设置为不可视
//        id_cartoons_read_header.isVisible=false
//        id_cartoons_read_footer.isVisible =false

        id_cartoons_read_header.iv_title_back.setOnClickListener(){
            this.finish()
        }
//
//                 设置左右手模式
        id_cartoons_read_footer.ll_left_or_right_hand.setOnClickListener(){
            if(id_cartoons_read_footer.tv_left_or_right_hand.text=="左手模式") {
                id_cartoons_read_footer.tv_left_or_right_hand.text = "右手模式"
                leftOrRightHand="Left"
                var pos=vp.currentItem
                vp.adapter = ViewData.RecyclerViewAdapter(this,vp,nowCartoonsInfo,nowChapter,nowChapterPageNum,id_cartoons_read_header, id_cartoons_read_footer,sb_read_cartoon_progress,leftOrRightHand,1)
                vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){})
                vp.setCurrentItem(pos,false)
            }
            else {
                id_cartoons_read_footer.tv_left_or_right_hand.text = "左手模式"
                var pos=vp.currentItem
                leftOrRightHand="Right"
                vp.adapter = ViewData.RecyclerViewAdapter(this,vp,nowCartoonsInfo,nowChapter,nowChapterPageNum,id_cartoons_read_header, id_cartoons_read_footer,sb_read_cartoon_progress,leftOrRightHand,1)
                vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){})
                vp.setCurrentItem(pos,false)
            }
        }

        id_cartoons_read_footer.ll_chapter_list.setOnClickListener(){

        }
        id_cartoons_read_footer.tv_system_brightness.setOnClickListener(){
            id_cartoons_read_footer.tv_system_brightness.isSelected = !id_cartoons_read_footer.tv_system_brightness.isSelected
        }


        sb_read_cartoon_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if(seekBar.progress==0)
                    seekBar.progress=1
                tv_page_process.text=seekBar.progress.toString()+"/"+seekBar.max
                Log.d("seekBar",progress.toString())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mcd_page_process.isVisible=true
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mcd_page_process.isVisible=false
                vp.setCurrentItem(seekBar.progress-1,false)
            }
        })

        //亮度调节
        val seekBar = findViewById<SeekBar>(R.id.sb_brightness_progress)
        val tvBrightFollowSystem = findViewById<View>(R.id.tv_system_brightness)
//        seekBar.progress = setting.getBrightProgress()
//        tvBrightFollowSystem.isSelected = setting.isBrightFollowSystem()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                this@CartoonReadActivity.setScreenBrightness(seekBar.progress.toFloat() / 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

//        亮度跟随系统
        tv_system_brightness.setOnClickListener(){
            tv_system_brightness.isSelected = !tv_system_brightness.isSelected
            if (tv_system_brightness.isSelected()) {
                setScreenBrightness(getSystemScreenBrightness(this@CartoonReadActivity).toFloat())
            }
            else{
//                设置为当面Seekbar的亮度
                this@CartoonReadActivity.setScreenBrightness(seekBar.progress.toFloat() / 100)
            }
        }
    }
    private fun initViews() {
    }
    class ViewData(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class RecyclerViewAdapter(val mcontext:Context ,val vp:ViewPager2, val nowCartoonsInfo:CartoonsInfo,val nowChapter:Int,val nowChapterPageNum:Int,
        val id_cartoons_read_header:View,val id_cartoons_read_footer:View,var seekBar: SeekBar,var leftOrRightHand:String,var isShowSetting:Int
        ): RecyclerView.Adapter<ViewData>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewData {
                return ViewData(LayoutInflater.from(parent.context).inflate(R.layout.page_cartoon_layout, parent, false))
            }
            @SuppressLint("RestrictedApi")
            override fun onBindViewHolder(holder: ViewData, position: Int) {
                vp.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT_DEFAULT)
                seekBar.max=getItemCount()
                seekBar.progress=position
//                val current = LocalDateTime.now() //用这个会有报错，主要是java.time.LocalDateTime包的问题。
            //获取系统时间
                val dateFormat = SimpleDateFormat("HH:mm")
                val curDate = Date(System.currentTimeMillis())
                val format = dateFormat.format(curDate)
                holder.itemView.read_time_TV.text = format
                // 页码：
                var strpage:String=(position+1).toString()+"/"+getItemCount().toString()+"  "
                holder.itemView.read_countNum_TV.text=strpage
                // 图片：
                val cartoonsSrc:String
                val cartoonsChapter:String
                val imgUrl = "http://192.168.137.1:8080/image/"+nowCartoonsInfo.cartoonURL+"/"+"第"+nowChapter+"话"+"/"+position   +".jpg"
                val image :ImageView= holder.itemView.onei
                val Context: Context = image.context
                Glide.with(Context).load(imgUrl).dontAnimate().into(image)

//                章节
                holder.itemView.read_title_TV.text=" "+nowChapter.toString()+"话 "

                id_cartoons_read_header.isVisible =false
                id_cartoons_read_footer.isVisible =false
//                这个isShowSetting主要是用来切换左右手后，更新leftOrRightHand全局变量，重新关联适配器，并且非smooth跳转后，重新显示一下Setting，以实现更好的交互效果。
//                喜提bug
//                快速解决bug
                if(isShowSetting==1)
                {
                    isShowSetting=0
                    id_cartoons_read_header.isVisible =true
                    id_cartoons_read_footer.isVisible =true
                }
                var x =  holder.itemView.onei.x
                holder.itemView.onei.setOnTouchListener(OnTouchListener {
                        v, event -> x=event.rawX
                    false })


                holder.itemView.onei.setOnClickListener()
                {
                    Log.d("x:",x.toString())
                    val y = holder.itemView.onei.y
                    Log.d("y:",y.toString())
                    var wid=holder.itemView.onei.right -holder.itemView.onei.left
                    Log.d("wid:",wid.toString())
                    if(leftOrRightHand=="Right") {
                        if (x < wid / 3) {
                            Log.d("tag", "左边")
                            vp.setCurrentItem(position - 1)
                        } else if (x > wid / 3.0 && x < wid * 2 / 3.0) {
                            id_cartoons_read_header.isVisible =
                                id_cartoons_read_header.isVisible != true
                            id_cartoons_read_footer.isVisible =
                                id_cartoons_read_footer.isVisible != true
//                    Toast.makeText(CartoonReadActivity.this, position + "", 1000).show();
                        } else {
                            Log.d("tag", "右边")
                            vp.setCurrentItem(position + 1)
                        }
                    }
                    else {
                        if (x < wid / 3) {
                            Log.d("tag", "左边")
                            vp.setCurrentItem(position + 1)
                        } else if (x > wid / 3.0 && x < wid * 2 / 3.0) {
                            id_cartoons_read_header.isVisible =
                                id_cartoons_read_header.isVisible != true
                            id_cartoons_read_footer.isVisible =
                                id_cartoons_read_footer.isVisible != true
//                    Toast.makeText(CartoonReadActivity.this, position + "", 1000).show();
                        } else {
                            Log.d("tag", "右边")
                            vp.setCurrentItem(position - 1)
                        }
                    }
                }
            }


            override fun getItemCount(): Int {
                return nowChapterPageNum
            }
        }
    }
}
