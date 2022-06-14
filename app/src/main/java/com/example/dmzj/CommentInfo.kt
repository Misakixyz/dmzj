package com.example.dmzj

import java.io.Serializable

//8个参数
data class CommentInfo (val userName:String, val commentInfo:String, val commentDate:String, val commentObjectName:String, val userHeadURL:String,
                        val userID:Int, val userSex:Int, val commentLikeNum:Int): Serializable