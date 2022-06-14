package com.example.dmzj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_novels_contents.*
import kotlinx.android.synthetic.main.novel_contents_elemlayout.view.*

class NovelsContentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val nowNovelInfo  = intent.getSerializableExtra("nowNovelInfo") as NovelInfo
        val nowChapter=intent.getIntExtra("nowChapter",1)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_novels_contents)
        val layoutManager= LinearLayoutManager(this)
        recyclerNovelContents.layoutManager =layoutManager
        val adapter = cartoonsUpdateAdapter(nowNovelInfo,nowNovelInfo.novelChapterNum)
        recyclerNovelContents.adapter=adapter
    }

    //    容器
    private class cartoonsUpdateViewHolder(view: View) : RecyclerView.ViewHolder(view){
    }
    //    适配器
    private class cartoonsUpdateAdapter(val nowNovelInfo: NovelInfo,val nowChapter:Int): RecyclerView.Adapter<cartoonsUpdateViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return nowChapter
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):cartoonsUpdateViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.novel_contents_elemlayout,parent,false)
            return cartoonsUpdateViewHolder(view)
        }
        override fun onBindViewHolder(holder: cartoonsUpdateViewHolder, position: Int) {

            holder.itemView.tv_novel_content.text="第"+(position+1)+"章"
            holder.itemView.setOnClickListener() {
//                Toast.makeText(holder.itemView.context,cartoonsUpdateList[position].cartoonDescription,Toast.LENGTH_LONG).show()
                val intent = Intent(holder.itemView.context, MyNovelsReadActivity::class.java)
                intent.putExtra("nowNovelInfo", nowNovelInfo)
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}