package com.example.dmzj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<String> list;
    private ArrayList<String>historyList =new ArrayList<String>();
    private ArrayList<String>showHistoryList =new ArrayList<String>();

    private TextView auto_tv;
    private ImageView select;
    private EditText etName;
    private TextView diss;
    private TextView delete;
    private String name;
    private AutoFlowLayout auto_layout;

    private ArrayList<CartoonsInfo> getCartoonList= new  ArrayList<CartoonsInfo>(),selectCartoonList=new  ArrayList<CartoonsInfo>();
    private RecyclerView recyclerviewSearch;
    private RecyclerView.Adapter mAdapter;

    private String searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        searchType= getIntent().getStringExtra("searchType");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search);
        initViews();
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    GetSearchHistoryByHttpURLConnection();
                }  catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new HistorySearchRecyclerAdapter(SearchActivity.this,showHistoryList);
                        recyclerviewSearch.setAdapter(mAdapter);
                    }
                });
            }
        };
        new Thread(runnable).start();


////        创建线性布局
//        mLayoutManager = new LinearLayoutManager(this);
////        给RecyclerView设置布局管理器
//        recyclerviewSearch.setLayoutManager(mLayoutManager);
//        //垂直方向
//        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //开始设置RecyclerView
//        recyclerviewSearch.addItemDecoration(new GridItemDecoration.Builder().spanCount(3)
//                .spaceSize(1).mDivider(new ColorDrawable(0x88ff0000)).build());

        recyclerviewSearch=(RecyclerView)this.findViewById(R.id.recyclerviewSearch);
        //设置固定大小
        recyclerviewSearch.setHasFixedSize(true);
        recyclerviewSearch.setLayoutManager(new GridLayoutManager(this, 2));//设置布局管理器
        recyclerviewSearch.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        //创建适配器，并且设置
        mAdapter = new HistorySearchRecyclerAdapter(this,showHistoryList);
        recyclerviewSearch.setAdapter(mAdapter);
        InitData();
    }

    public class HistorySearchRecyclerAdapter extends RecyclerView.Adapter<HistorySearchRecyclerAdapter.ViewHolder>{

        private LayoutInflater mInflater;
        public HistorySearchRecyclerAdapter(Context context, List<String> showHistoryList ){
            this.mInflater=LayoutInflater.from(context);

        }
        /**
         * item显示类型
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=mInflater.inflate(R.layout.history_search_layout,parent,false);
            //view.setBackgroundColor(Color.RED);
            ViewHolder viewHolder=new ViewHolder(view);
            return viewHolder;
        }
        /**
         * 数据的绑定显示
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv_history_search.setText(showHistoryList.get(position));
            holder.tv_history_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etName.setText(holder.tv_history_search.getText());
                    Runnable runnable = new Runnable() {
                        public void run() {
                            try {
                                addSearchHistoryByHttpURLConnection();
                            }  catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new HistorySearchRecyclerAdapter(SearchActivity.this,showHistoryList);
                                    recyclerviewSearch.setAdapter(mAdapter);
                                }
                            });
                        }
                    };
                    new Thread(runnable).start();
                    Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                    intent.putExtra("searchType",searchType);
                    intent.putExtra("searchKeywords",etName.getText().toString());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return showHistoryList.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public  class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_history_search;
            public ViewHolder(View view){
                super(view);
                tv_history_search = (TextView)view.findViewById(R.id.tv_history_search);
            }
        }
    }
    private void initViews() {
        list = new ArrayList<>();
        auto_tv = findViewById(R.id.auto_tv);
        select = findViewById (R.id.select);
        etName = findViewById (R.id.et_name);
        auto_layout = findViewById (R.id.auto_layout);
        diss = findViewById (R.id.diss);
        delete = findViewById (R.id.delete);
        select.setOnClickListener(this);
        diss.setOnClickListener(this);
        delete.setOnClickListener(this);

        etName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                name = etName.getText ().toString ();

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {   // 按下完成按钮，这里和上面imeOptions对应
                    Runnable runnable = new Runnable() {
                        public void run() {
                            try {
                                addSearchHistoryByHttpURLConnection();
                            }  catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new HistorySearchRecyclerAdapter(SearchActivity.this,showHistoryList);
                                    recyclerviewSearch.setAdapter(mAdapter);
                                }
                            });
                        }
                    };
                    new Thread(runnable).start();
                    Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                    intent.putExtra("searchType",searchType);
                    intent.putExtra("searchKeywords",etName.getText().toString());
                    startActivity(intent);
                    return false;   //返回true，保留软键盘。false，隐藏软键盘
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.select://搜索
                name = etName.getText ().toString ();
//                list.add (name);
                historyList.add(name);
//                auto();
                mAdapter.notifyDataSetChanged();
                Runnable runnable = new Runnable() {
                    public void run() {
                        try {
                            addSearchHistoryByHttpURLConnection();
                        }  catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new HistorySearchRecyclerAdapter(SearchActivity.this,showHistoryList);
                                recyclerviewSearch.setAdapter(mAdapter);
                            }
                        });
                    }
                };
                new Thread(runnable).start();
                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                intent.putExtra("searchType",searchType);
                intent.putExtra("searchKeywords",etName.getText().toString());
                startActivity(intent);

                break;
            case R.id.diss://取消
                finish();
                break;
            case R.id.delete://删除
                Runnable runnable2 = new Runnable() {
                    public void run() {
                        try {
                            DeleteSearchHistoryByHttpURLConnection();
                        }  catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new HistorySearchRecyclerAdapter(SearchActivity.this,showHistoryList);
                                recyclerviewSearch.setAdapter(mAdapter);
                            }
                        });
                    }
                };
                new Thread(runnable2).start();

                etName.getText ().clear ();
                historyList.clear ();
                showHistoryList.clear ();

                mAdapter.notifyDataSetChanged();
//                auto_layout.removeAllViews ();
                break;
        }
    }
//
    private void auto() {
        auto_layout.setAdapter(new FlowAdapter(list) {
            private View view;
            @Override
            public View getView(int i) {
                if(list != null){
                    view = View.inflate(SearchActivity.this, R.layout.layout_auto, null);
                    final TextView auto_tv = view.findViewById(R.id.auto_tv);
                    String tempStr=list.get(i);
                    auto_tv.setText("   "+list.get(i)+"   ");
                    Random random = new Random();//指定种子数字
                    Integer randomNum=random.nextInt(3);
//                    深蓝中蓝，浅蓝
                    if(randomNum==0)
                     auto_tv.setBackgroundColor(Color.rgb(38,117,246));
                    if(randomNum==1)
                        auto_tv.setBackgroundColor(Color.rgb(0,151,255));
                    if(randomNum==2)
                        auto_tv.setBackgroundColor(Color.rgb(64,182,254));
                    auto_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {
                            etName.setText(tempStr);
                            name =etName.getText().toString();
                            historyList.add(tempStr);
                            mAdapter.notifyDataSetChanged();
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    try {
                                        addSearchHistoryByHttpURLConnection();
                                    }  catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter = new HistorySearchRecyclerAdapter(SearchActivity.this,showHistoryList);
                                            recyclerviewSearch.setAdapter(mAdapter);
                                        }
                                    });
                                }
                            };
                            new Thread(runnable).start();



                            Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                            intent.putExtra("searchType",searchType);
                            intent.putExtra("searchKeywords",tempStr);

                            startActivity(intent);
                        }
                    });
//                    list.clear();
                }

                return view;
            }
        });
    }
    private void InitData(){
        if(searchType.equals("NovelActivity")==false) {
            list.add("妖神记");
            list.add("猫之茗");
            list.add("头条都是他");
            list.add("一条狗");
            list.add("唐山葬");
            list.add("笨柴兄弟");
            list.add("杀老师Quest");
            list.add("地狱的19层");
            list.add("盛气凌人");
            list.add("地狱的19层");
            list.add("盛气凌人");
            list.add("Online");
            list.add("班长大人");
            list.add("绝对恋爱命令");
            list.add("日常幻想指南");
            list.add("雏鸟的华尔兹");
            list.add("仙侠世界");
            list.add("龙珠AF");
        }
        else{
            list.add("异世界狂想曲");
            list.add("精灵使的剑舞");
            list.add("我的怪物眷族");
            list.add("那个人，后来");
            list.add("零之使魔");
            list.add("我的朋友很少");
            list.add("要听爸爸的话");
            list.add("IS Infinite Stratos");
            list.add("愿你手拥幸福");
            list.add("黑之魔王");
            list.add("狼与香辛料");
            list.add("这个是僵尸吗");
            list.add("贤者之孙");
            list.add("魔装血缘HxH");
            list.add("文学少女");
            list.add("86-Eight Six -");
            list.add("Only Sense Online");
            list.add("少年阴阳师");
        }
        auto();
    }
    public void getRequest() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.137.1:8080/getCartoonUpdate.jsp")
                .build()
                ;
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
//            System.out.println(response.body().string());
            getCartoonList.clear();
            String jsonString=response.body().string();
            JSONArray jsonArray=new JSONArray(jsonString);
            for(int i=0;i<jsonArray.length();i++){		//一个循环代表一个headnews对象
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String cartoonName=jsonObject.getString("cartoonName");
                String cartoonDescription=jsonObject.getString("cartoonDescription");
                String cartoonAuthor=jsonObject.getString("cartoonAuthor");
                String cartoonURL=jsonObject.getString("cartoonURL");
                String cartoonCoverURL=jsonObject.getString("cartoonCoverURL");
                String cartoonLastUpdateTime=jsonObject.getString("cartoonLastUpdateTime");
                String cartoonType=jsonObject.getString("cartoonType");

                Integer cartoonID=Integer.parseInt(jsonObject.getString("cartoonID"));
                Integer cartoonChapterNum=Integer.parseInt(jsonObject.getString("cartoonChapterNum"));
                Integer cartoonHot=Integer.parseInt(jsonObject.getString("cartoonHot"));
                Integer cartoonSubscriptionsNum=Integer.parseInt(jsonObject.getString("cartoonSubscriptionsNum"));
                Integer cartoonCommentNum=Integer.parseInt(jsonObject.getString("cartoonCommentNum"));
                Integer cartoonIsFinished=Integer.parseInt(jsonObject.getString("cartoonIsFinished"));
//                CartoonsInfo cartoonsInfo=new CartoonsInfo("","");
//                list.add(cartoonsInfo);
                getCartoonList.add(new CartoonsInfo(cartoonName,cartoonDescription,cartoonAuthor,cartoonURL,cartoonCoverURL,cartoonLastUpdateTime,cartoonType,
                        cartoonID,cartoonChapterNum,cartoonHot,cartoonSubscriptionsNum,cartoonCommentNum,cartoonIsFinished));
                System.out.println(getCartoonList);
            }

        }

    }
    public void GetSearchHistoryByHttpURLConnection()throws Exception{

        List<CartoonsInfo> list=new ArrayList<CartoonsInfo>();

        String path="http://192.168.137.1:8080/getSearchHistory.jsp";
        //参数直接加载url后面
        if(searchType.equals("NovelActivity"))
            path+="?search_history_type="+ URLEncoder.encode("novel_search_history","utf-8");
        else
            path+="?search_history_type="+ URLEncoder.encode("cartoon_search_history","utf-8");
        URL url=new URL(path);
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        if(conn.getResponseCode()==200)
        {				//200表示请求成功
            InputStream is=conn.getInputStream();		//以输入流的形式返回
            //将输入流转换成字符串
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte [] buffer=new byte[1024];
            int len=0;
            while((len=is.read(buffer))!=-1){
                baos.write(buffer, 0, len);
            }
            String jsonString=baos.toString();
            baos.close();
            is.close();
            //转换成json数据处理
            JSONArray jsonArray=new JSONArray(jsonString);
            for(int i=0;i<jsonArray.length();i++){		//一个循环代表一个headnews对象
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String search_history=jsonObject.getString("search_history");
                historyList.add(search_history);
            }
            for (int i=0;i<10;i++){
                showHistoryList.add(historyList.get(historyList.size()-i-1));
            }
        }

    }
    public void addSearchHistoryByHttpURLConnection()throws Exception{

        List<CartoonsInfo> list=new ArrayList<CartoonsInfo>();

        String path="http://192.168.137.1:8080/addSearchHistory.jsp";
        //参数直接加载url后面
        if(searchType.equals("NovelActivity"))
            path+="?search_history_type="+ URLEncoder.encode("novel_search_history","utf-8");
        else
            path+="?search_history_type="+ URLEncoder.encode("cartoon_search_history","utf-8");
        path+="&search_history_info="+  URLEncoder.encode(name,"utf-8");
        URL url=new URL(path);
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        if(conn.getResponseCode()==200)
        {				//200表示请求成功
            InputStream is=conn.getInputStream();		//以输入流的形式返回
            //将输入流转换成字符串
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte [] buffer=new byte[1024];
            int len=0;
            while((len=is.read(buffer))!=-1){
                baos.write(buffer, 0, len);
            }
            String jsonString=baos.toString();
            baos.close();
            is.close();
        }
    }
    public void DeleteSearchHistoryByHttpURLConnection()throws Exception{

        List<CartoonsInfo> list=new ArrayList<CartoonsInfo>();

        String path="http://192.168.137.1:8080/deleteSearchHistory.jsp";
        //参数直接加载url后面
        if(searchType.equals("NovelActivity"))
            path+="?search_history_type="+ URLEncoder.encode("novel_search_history","utf-8");
        else
            path+="?search_history_type="+ URLEncoder.encode("cartoon_search_history","utf-8");
        URL url=new URL(path);
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        if(conn.getResponseCode()==200)
        {				//200表示请求成功
            InputStream is=conn.getInputStream();		//以输入流的形式返回
            //将输入流转换成字符串
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte [] buffer=new byte[1024];
            int len=0;
            while((len=is.read(buffer))!=-1){
                baos.write(buffer, 0, len);
            }
            String jsonString=baos.toString();
            baos.close();
            is.close();
        }

    }
}
