package com.example.dmzj

import java.io.Serializable

class NewsInfo (val newsName:String, val newsAuthor:String,val newsAuthorHeadURL: String ,val newsURL:String, val newsCoverURL:String, val newsTime:String,val newsType:String,
                val newsID:Int, val newsCollectNum:Int, val newsCommentNum:Int,val newsLikeNum:Int) :
    Serializable