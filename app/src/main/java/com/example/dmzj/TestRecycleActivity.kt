package com.example.dmzj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_test_recycle.*
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.chapter_elem_layout.view.*


class TestRecycleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_recycle)
        val layoutManager = GridLayoutManager(this, 4) //第二个参数为网格的列数
        recyclerviewTest.layoutManager =layoutManager
        val adapter = TestRecycleAdapter(41)
        recyclerviewTest.adapter=adapter
    }
    private class TestRecycleViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val btn: Button = view.findViewById(R.id.btn_test)
    }
    //    适配器
    private class TestRecycleAdapter(val chapterNum:Int): RecyclerView.Adapter<TestRecycleViewHolder>(){
        //      列表行数
        override fun getItemCount(): Int {
            return chapterNum
        }
        //      创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestRecycleViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.chapter_elem_layout,parent,false)
            return TestRecycleViewHolder(view)
        }
        override fun onBindViewHolder(holder: TestRecycleViewHolder, position: Int) {
                holder.itemView.btn_test.text= "第"+position+"话"
        }
    }
}