package com.example.dmzj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    var userID:String=""
    var password:String=""
    var loginResult:Int =1
    private val userList:ArrayList<UserInfo> =ArrayList<UserInfo>()
    lateinit var userInfo: UserInfo
    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuffer()
                val urlStr:String="http://192.168.137.1:8080/getUserInfo.jsp?userID="+userID+"&password="+password
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
                refresh(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
    private fun refresh(jsonData:String){
        try {
            //json数组
            val jsonArray= JSONArray(jsonData)
            userList.clear()
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                val userName=jsonObject.getString("userName")
                val userHeadURL=jsonObject.getString("userHeadURL")
                val userSignature=jsonObject.getString("userSignature")
                val userPassword=jsonObject.getString("userPassword")
                val userBirthday=jsonObject.getString("userBirthday")
                val userConstellation=jsonObject.getString("userConstellation")
                val userBloodType=jsonObject.getString("userBloodType")
                val userID:Int=jsonObject.getString("userID").toInt()
                val userSex:Int=jsonObject.getString("userSex").toInt()
                val userLevel:Int=jsonObject.getString("userLevel").toInt()
                Log.d("UpdateActivity","打印信息：$userHeadURL")
                userList.add(
                    UserInfo(userName,userHeadURL,userSignature,userPassword,userBirthday,userConstellation,userBloodType,
                        userID,userSex,userLevel))
                userInfo=UserInfo(userName,userHeadURL,userSignature,userPassword,userBirthday,userConstellation,userBloodType,
                    userID,userSex,userLevel)
            }

            if(userInfo.userID.toString().equals(et_userID.text.toString()) && userInfo.userPassword.equals(et_password.text.toString()))
            {
                var globalUser: GlobalUser = application as GlobalUser
                globalUser.userName=userInfo.userName
                globalUser.userHeadURL=userInfo.userHeadURL
                globalUser.userSignature=userInfo.userSignature
                globalUser.userPassword=userInfo.userPassword
                globalUser.userBirthday=userInfo.userBirthday
                globalUser.userConstellation=userInfo.userConstellation
                globalUser.userBloodType=userInfo.userBloodType
                globalUser.userSex=userInfo.userSex
                globalUser.userID=userInfo.userID
                globalUser.userLevel=userInfo.userLevel
                Log.d("登录","$userInfo")
                loginResult=1
//                val intent = Intent(this,MainActivity::class.java)
//                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                loginResult=0
            }
            run{
                if(loginResult==1) {
                    Looper.prepare()
                    val toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
                    toast.setText("登录成功~")
                    toast.show()
                    Looper.loop()
                }
                if(loginResult==0){
                    Looper.prepare()
                    val toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
                    toast.setText("登录失败~")
                    toast.show()
                    Looper.loop()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        btn_my_login.setOnClickListener(){
            userID=et_userID.text.toString()
            password=et_password.text.toString()
            sendRequestWithHttpURLConnection()


//                val toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
//                toast.setText("登录中~")
//                toast.show()

        }
    }
}