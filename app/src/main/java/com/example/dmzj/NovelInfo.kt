package com.example.dmzj

import java.io.Serializable

data class NovelInfo  (val novelName:String, val novelDescription:String, val novelAuthor:String, val novelURL:String, val novelCoverURL:String, val novelLastUpdateTime:String, val novelType:String,
                       val novelID:Int, val novelChapterNum:Int, val novelHot:Int, val novelSubscriptionsNum:Int, val novelCommentNum:Int, val novelIsFinished:Int) :
    Serializable
