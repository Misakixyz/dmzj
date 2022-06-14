package com.example.dmzj

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_comment_detail.*

class CommentDetailActivity : AppCompatActivity() {
    var likeStyle=false
    override fun onCreate(savedInstanceState: Bundle?) {
        var commentInfo:CommentInfo=  intent.getSerializableExtra("CommentInfo") as CommentInfo
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_detail)
        supportActionBar?.hide()
        iv_back.setOnClickListener()
        {
            this.finish()
        }
        item_info_iv_reply.text=commentInfo.commentInfo
        item_info_iv_user_name.text=commentInfo.userName
        item_info_iv_reply_time.text=commentInfo.commentDate
        tv_like_num.text=commentInfo.commentLikeNum.toString()
        //头像
        val imgUrl = "http://192.168.137.1:8080/user_head/"+commentInfo.userHeadURL+".png"
        val image : ImageView =item_info_iv
        val Context: Context = image.context
        Glide.with(Context).load(imgUrl).dontAnimate().into(image)
//            性别
        if(commentInfo.userSex==0)
            iv_user_sex.setImageResource(R.drawable.sex_female)
        else
            iv_user_sex.setImageResource(R.drawable.sex_male)
        iv_like_style.setOnClickListener() {
            if (likeStyle == false) {
                iv_like_style.setImageResource(R.drawable.like_true)
                tv_like_num.text =
                    (tv_like_num.text.toString().toInt() + 1).toString()
                likeStyle = true
            } else {
                iv_like_style.setImageResource(R.drawable.like_false)
                tv_like_num.text =
                    (tv_like_num.text.toString().toInt() - 1).toString()
                likeStyle= false
            }
        }
    }
}