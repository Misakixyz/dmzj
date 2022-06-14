package com.example.dmzj
import java.util.HashMap;
import android.app.Application;

// 7个String
// 3个Int

class GlobalUser() : Application() {
     var userName: String=""
     var userHeadURL: String=""
     var userSignature: String=""
     var userPassword: String=""
     var userBirthday: String=""
     var userConstellation: String=""
     var userBloodType: String=""
    var userID: Int = 0
    var userSex: Int = 0
    var userLevel: Int = 0
    constructor(
        userName: String,
        userHeadURL: String,
        userSignature: String,
        userPassword: String,
        userBirthday: String,
        userConstellation: String,
        userBloodType: String,
        userID: Int,
        userSex: Int,
        userLevel: Int
    ) : this() {
        this.userName=userName
        this.userHeadURL=userHeadURL
        this.userSignature=userSignature
        this.userPassword=userPassword
        this.userBirthday=userBirthday
        this.userConstellation=userConstellation
        this.userBloodType=userBloodType
        this.userID=userID
        this.userSex=userSex
        this.userLevel=userLevel
    }

    override fun onCreate() {
        super.onCreate()
    }
}