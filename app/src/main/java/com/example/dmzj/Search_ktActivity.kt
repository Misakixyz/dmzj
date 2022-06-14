package com.example.dmzj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.library.AutoFlowLayout
import com.example.library.FlowAdapter

class Search_ktActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var list: ArrayList<String>
    lateinit var auto_tv: TextView
    lateinit var select: ImageView
    lateinit var etName: EditText
    lateinit var diss: TextView
    lateinit var delete: ImageView
    lateinit var name: String
    lateinit var auto_layout: AutoFlowLayout<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_kt)
        initViews()
    }

    private fun initViews() {
        list = ArrayList<String>()
        auto_tv = findViewById(R.id.auto_tv)
        select = findViewById(R.id.select)
        etName = findViewById(R.id.et_name)
        auto_layout = findViewById(R.id.auto_layout)
        diss = findViewById(R.id.diss)
        delete = findViewById(R.id.delete)
        select.setOnClickListener(this)
        diss.setOnClickListener(this)
        delete.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.select -> {
                name = etName.text.toString()
                list.add(name)
                auto()
            }
            R.id.diss -> {
                etName.text.clear()
                list.clear()
            }
            R.id.delete -> {
                etName.text.clear()
                list.clear()
                auto_layout.removeAllViews()
            }
        }
    }

    private fun auto() {

        auto_layout.setAdapter(object : FlowAdapter<String>(list) {
            lateinit  var view: View
            override fun getView(i: Int): View {
                if (list != null) {
                    view = View.inflate(this@Search_ktActivity, R.layout.layout_auto, null)
                    val auto_tv = view.findViewById<TextView>(R.id.auto_tv)
                    auto_tv.text = list[i]
                    auto_tv.setOnClickListener {
                        val intent = Intent(this@Search_ktActivity, MainActivity::class.java)
                        intent.putExtra("name", name)
                        startActivity(intent)
                    }
                    list.clear()
                }
                return view
            }
        } as Nothing)
    }
}