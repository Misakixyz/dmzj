package com.example.dmzj

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_mine_user_info.*
import kotlinx.android.synthetic.main.activity_mine_user_info.head_iv

class MineUserInfoActivity : AppCompatActivity() {
    var nowUserName=""
    var nowUserSex=1
    var nowUserHeadURL=""
    var nowUserSignature=""
    var nowUserBirthday=""
    var nowUserConstellation=""
    var nowUserBloodType=""
    var nowUserID=0
    fun initUser(){
        val globalUser: GlobalUser = application as GlobalUser
        nowUserName=globalUser.userName
        nowUserSex=globalUser.userSex
        nowUserHeadURL=globalUser.userHeadURL
        nowUserID=globalUser.userID
        nowUserSignature=globalUser.userSignature
        nowUserBirthday=globalUser.userBirthday
        nowUserConstellation=globalUser.userConstellation
        nowUserBloodType=globalUser.userBloodType
    }
    fun initUI(){
//        头像
        val imgUrl = "http://192.168.137.1:8080/user_head/"+nowUserHeadURL+".png"
        val image : ImageView =head_iv
        val context: Context = image.context
        Glide.with(context).load(imgUrl).dontAnimate().into(image)
//        名字
//        tv_user_name.text="编辑头像"
        about_name.text=about_name.text.toString()+":"+nowUserName
        about_note.text=about_note.text.toString()+":"+nowUserSignature
        about_sex.text=about_sex.text.toString()+":"+nowUserSex.toString()
        about_birth.text=about_birth.text.toString()+":"+nowUserBirthday
        about_star.text=about_star.text.toString()+":"+nowUserConstellation
        about_bloodtype.text=about_bloodtype.text.toString()+":"+nowUserBloodType
        head_iv.setOnClickListener() {

        }
        rl_mine_user_info.setOnClickListener(){
            var intent = Intent(this, MineUserInfoEditActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        initUser()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_mine_user_info)
        initUI()
    }
}