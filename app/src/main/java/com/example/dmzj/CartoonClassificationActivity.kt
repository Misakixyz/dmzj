package com.example.dmzj

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_classification.*
import kotlinx.android.synthetic.main.header_layout_cartoons.*

class CartoonClassificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏标题栏
        supportActionBar?.hide()
        setContentView(R.layout.activity_classification)
        initViews()
        iv_cartoons_classification_shaonianman.setOnClickListener()
        {
            val intent = Intent(this,ClassificationPageActivity::class.java)
            intent.putExtra("cartoonType","国漫")
            startActivity(intent)
        }
        iv_cartoons_classification_guoman.setOnClickListener()
        {
            val intent = Intent(this,ClassificationPageActivity::class.java)
            intent.putExtra("cartoonType","国漫")
            startActivity(intent)
        }
    }

    private fun initViews() {
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
        btnHeaderClassification.setTextColor(Color.rgb(0,191,255))
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
            intent.putExtra("searchType","CartoonClassificationActivity")
            startActivity(intent)
        }

    }


}