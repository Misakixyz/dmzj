package com.example.dmzj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mine_user_info.*
import kotlinx.android.synthetic.main.activity_mine_user_info_edit.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class MineUserInfoEditActivity : AppCompatActivity() {
    var nowUserName=""
    var nowUserSex=1
    var nowUserSignature=""
    var nowUserBirthday=""
    var nowUserConstellation=""
    var nowUserBloodType=""
    var nowUserPassword=""
    var nowUserID=0
    fun initUser() {
        val globalUser: GlobalUser = application as GlobalUser
        nowUserName = et_user_name.text.toString()
        if (et_user_sex.text.toString().equals("男"))
            nowUserSex = 1
        else
            nowUserSex = 0
        nowUserID = globalUser.userID
        nowUserSignature = et_user_signature.text.toString()
        nowUserBirthday = et_user_birthday.text.toString()
        nowUserConstellation = et_user_constellation.text.toString()
        nowUserBloodType = et_user_bloodType.text.toString()
        nowUserPassword=et_password.text.toString()
    }
    private fun updateUserInfoByRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()

                val urlStr:String="http://192.168.137.1:8080/updateUserInfo.jsp"+
                        "?user_ID="+ nowUserID+
                        "&user_name="+ nowUserName+
                        "&user_sex="+ nowUserSex+
                        "&user_signature="+ nowUserSignature+
                        "&user_birthday="+ nowUserBirthday+
                        "&user_constellation="+ nowUserConstellation+
                        "&user_blood_type="+ nowUserBloodType+
                        "&user_password="+ nowUserPassword;

                val url = URL(urlStr)
                connection = url.openConnection() as HttpURLConnection
                connection.setRequestMethod("GET")
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                val input = connection.inputStream
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                var globalUser: GlobalUser = application as GlobalUser
                globalUser.userName=nowUserName
                globalUser.userSignature=nowUserSignature
                globalUser.userPassword=nowUserPassword
                globalUser.userBirthday=nowUserBirthday
                globalUser.userConstellation=nowUserConstellation
                globalUser.userBloodType=nowUserBloodType
                globalUser.userSex=nowUserSex
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_mine_user_info_edit)

//        change
        btn_mine_user_info_change.setOnClickListener(){
            initUser()
            updateUserInfoByRequestWithHttpURLConnection()
            Toast.makeText(this,"修改信息成功~",Toast.LENGTH_LONG).show()
            var intent = Intent(this, MineUserInfoActivity::class.java)
            startActivity(intent)
        }
//        return
        tv_mine_user_info_edit_return.setOnClickListener(){
            var intent = Intent(this, MineUserInfoActivity::class.java)
            startActivity(intent)
        }
    }

}