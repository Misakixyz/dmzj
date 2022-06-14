package com.example.dmzj

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
//import kotlinx.android.synthetic.main.activity_second.view.*
import kotlinx.android.synthetic.main.activity_test_tomcat_image.*

class TestTomcatImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_tomcat_image)
        for(index in 0..2) {
            val imgUrl = "http://10.151.148.125:8080/image/shaonv/Chapter41/" + index + ".jpg"
            val image: ImageView = findViewById(R.id.image)
            val Context: Context = image.context
            Glide.with(Context).load(imgUrl).dontAnimate().into(image)

//        println(imgUrl)
            Toast.makeText(this, imgUrl, Toast.LENGTH_LONG).show()
        }
    }
}