package com.example.dmzj

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_mine.*
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.activity_user_info.head_iv
import kotlinx.android.synthetic.main.user_info_head_layout.*

class MineActivity : AppCompatActivity() {
    var nowUserName=""
    var nowUserSex=1
    var nowUserHeadURL=""
    var nowUserID=0
    fun initCommentUser(){
        val globalUser: GlobalUser = application as GlobalUser
        nowUserName=globalUser.userName
        nowUserSex=globalUser.userSex
        nowUserHeadURL=globalUser.userHeadURL
        nowUserID=globalUser.userID
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_mine)
        initCommentUser()
        initUI()
        initViews()
        initFunViews()
    }
    fun initUI(){
        val imgUrl = "http://192.168.137.1:8080/user_head/"+nowUserHeadURL+".png"
        val image : ImageView =head_iv
        val context: Context = image.context
        Glide.with(context).load(imgUrl).dontAnimate().into(image)
        tv_user_name.text=nowUserName
        head_iv.setOnClickListener() {
            if (nowUserID == 0) {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                var intent = Intent(this, MineUserInfoActivity::class.java)
                startActivity(intent)
            }
        }
    }
    fun initFunViews(){
        rl_novel_subscribe.setOnClickListener(){
            val intent =Intent(this,MineNovelSubscribeActivity::class.java)
            startActivity(intent)
        }
        rl_cartoon_subscribe.setOnClickListener(){
            val intent =Intent(this,MineCartoonSubscribeActivity::class.java)
            startActivity(intent)
        }
        rl_cartoon_comment.setOnClickListener(){
            val intent =Intent(this,MineCartoonCommentActivity::class.java)
            startActivity(intent)
        }
    }
    fun initViews(){
        // footer listener.
        // 漫画界面的绑定
        val btnFooterCartoons: ImageView =findViewById(R.id.iv_footer_cartoons)
        val btnFooterNews: ImageView =findViewById(R.id.iv_footer_news)
        val btnFooterNovels: ImageView =findViewById(R.id.iv_footer_novels)
        val btnFooterMine: ImageView =findViewById(R.id.iv_footer_mine)

        btnFooterMine.setImageResource(R.drawable.footer_mine_blue)
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
    }
}