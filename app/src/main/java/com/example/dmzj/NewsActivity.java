package com.example.dmzj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;



public class NewsActivity extends AppCompatActivity {


    private TabLayout myTab;
    private ViewPager2 myPager2;

    List<String> titles=new ArrayList<>();
    List<Fragment> fragments=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_news);
        myTab = findViewById(R.id.my_tab);
        myPager2 = findViewById(R.id.my_pager2);

        //添加标题
        titles.add("推荐");
        titles.add("动画情报");
        titles.add("漫画情报");
        titles.add("轻小说情报");
        titles.add("美图欣赏");
        titles.add("声优情报");
        titles.add("漫展情报");

        //添加Fragment
        fragments.add(new MyFragment());
        fragments.add(new fragment_animation());
        fragments.add(new fragment_cartoon());
        fragments.add(new fragment_novel());
        fragments.add(new fragment_picture());
        fragments.add(new fragment_sound());
        fragments.add(new fragment_exhibition());

        //实例化适配器
        MyAdapter myAdapter=new MyAdapter(getSupportFragmentManager(),getLifecycle(),fragments);
        //设置适配器
        myPager2.setAdapter(myAdapter);
        //TabLayout和Viewpager2进行关联
        new TabLayoutMediator(myTab, myPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();
        initViews();



    }
    private void initViews(){
        ImageView iv_footer_cartoons =findViewById(R.id.iv_footer_cartoons);
        ImageView iv_footer_news =findViewById(R.id.iv_footer_news);
        ImageView iv_footer_novels =findViewById(R.id.iv_footer_novels);
        ImageView iv_footer_mine =findViewById(R.id.iv_footer_mine);

        iv_footer_news.setImageResource(R.drawable.footer_news_blue);
        
        iv_footer_cartoons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
//        iv_footer_news.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(),NewsReadActivity.class);
//                startActivity(intent);
//            }
//        });
        iv_footer_novels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),NovelActivity.class);
                startActivity(intent);
            }
        });
        iv_footer_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MineActivity.class);
                startActivity(intent);
            }
        });

    }



}


