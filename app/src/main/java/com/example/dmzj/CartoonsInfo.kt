package com.example.dmzj

import java.io.Serializable

// 十三个参数
data class CartoonsInfo  (val cartoonName:String, val cartoonDescription:String, val cartoonAuthor:String, val cartoonURL:String, val cartoonCoverURL:String, val cartoonLastUpdateTime:String, val cartoonType:String,
                         val cartoonID:Int, val cartoonChapterNum:Int, val cartoonHot:Int, val cartoonSubscriptionsNum:Int, val cartoonCommentNum:Int, val cartoonIsFinished:Int) : Serializable
