package com.example.dmzj
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.graphics.Color
//import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

class RecommendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏标题栏
        supportActionBar?.hide()
        setContentView(R.layout.activity_recommend)
        initViews()
        val arrayImageUrl = arrayListOf<String>("https://images.cnblogs.com/cnblogs_com/Gealach/1632018/o_200113094540qq.jpg",
            "https://images.cnblogs.com/cnblogs_com/Gealach/1632018/o_2001131054282020-01-06%20(4).png",
            "https://images.cnblogs.com/cnblogs_com/Gealach/1632018/o_200113120940QQ%E5%9B%BE%E7%89%8720200113200908.jpg",
            "https://images.cnblogs.com/cnblogs_com/Gealach/1632018/o_200113120935QQ%E5%9B%BE%E7%89%8720200113200857.jpg",
            "https://images.cnblogs.com/cnblogs_com/Gealach/1632018/o_200113120935QQ%E5%9B%BE%E7%89%8720200113200857.jpg")
        val arrayTitle = arrayListOf<String>("燕燕天下第一可爱吖", "燕燕天下第一可爱吖", "燕燕天下第一可爱吖","燕燕天下第一可爱吖","燕燕天下第一可爱吖")

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
//        //设置图片加载器，图片加载器在下方
//        banner.setImageLoader(MyLoader())
//        //设置图片网址或地址的集合
//        banner.setImages(arrayImageUrl)
//        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
//        banner.setBannerAnimation(Transformer.Default)
//        //设置轮播图的标题集合
//        banner.setBannerTitles(arrayTitle)
//        //设置轮播间隔时间
//        banner.setDelayTime(3000)
//        //设置是否为自动轮播，默认是“是”。
//        banner.isAutoPlay(true)
//        //设置指示器的位置，小点点，左中右。
//        banner.setIndicatorGravity(BannerConfig.CENTER)
//        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//
//        banner.setOnBannerListener {
//            Log.d("=*=", "第几张" + it.dec())
//        }
//        //必须最后调用的方法，启动轮播图。
//        banner.start()
    }
    private fun initBanner() {
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
        val btnHeaderClassification:TextView =findViewById(R.id.tv_header_classification)
        val btnHeaderRank:TextView =findViewById(R.id.tv_header_rank)
//        val btnHeaderWelfare:TextView =findViewById(R.id.tv_header_welfare)
        val btnHeaderUpdate:TextView =findViewById(R.id.tv_update)
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
            val intent =Intent(this,CartoonReadActivity::class.java)
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
    }
}
