package com.example.dmzj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class fragment_exhibition extends Fragment {

    private RecyclerView recyclerviewNewsRecommend;
    private RecyclerView.Adapter mAdapter;
    //    private ArrayList<String> stringList=new ArrayList<String>();
    private ArrayList<NewsInfo> newsInfoList=new ArrayList<NewsInfo>();
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private String newsType="漫展情报";
    public void GetNewsInfoByHttpURLConnection()throws Exception{

        List<CartoonsInfo> list=new ArrayList<CartoonsInfo>();

        String path="http://192.168.137.1:8080/getNewsInfo.jsp?newsType="+newsType;
        //参数直接加载url后面
//        path+="?search_history_type="+ URLEncoder.encode("novel_search_history","utf-8");
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
            newsInfoList.clear();
            for(int i=0;i<jsonArray.length();i++){		//一个循环代表一个headnews对象
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String news_name=jsonObject.getString("news_name");
                String news_author=jsonObject.getString("news_author");
                String news_url=jsonObject.getString("news_url");
                String news_cover_url=jsonObject.getString("news_cover_url");
                String news_time=jsonObject.getString("news_time");
                String news_author_head=jsonObject.getString("news_author_head");
                String news_type=jsonObject.getString("news_type");
                Integer news_id= Integer.valueOf(jsonObject.getString("news_id"));
                Integer news_collect_num= Integer.valueOf(jsonObject.getString("news_collect_num"));
                Integer news_comment_num= Integer.valueOf(jsonObject.getString("news_comment_num"));
                Integer news_like_num= Integer.valueOf(jsonObject.getString("news_like_num"));

                newsInfoList.add(new NewsInfo(news_name,news_author,news_author_head,news_url,news_cover_url,news_time,news_type,
                        news_id,news_collect_num,news_comment_num,news_like_num));
            }

        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_exhibition, null);

//        for(int i=0;i<9;++i)
//        stringList.add("上音");

        recyclerviewNewsRecommend = (RecyclerView)inflate.findViewById(R.id.recyclerNewsExhibition);
        recyclerviewNewsRecommend.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mAdapter=new NewsRecommendRecyclerAdapter(getActivity(),newsInfoList);
        recyclerviewNewsRecommend.setAdapter(mAdapter);

        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    GetNewsInfoByHttpURLConnection();
                }  catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new NewsRecommendRecyclerAdapter(getActivity(),newsInfoList);
                        recyclerviewNewsRecommend.setAdapter(mAdapter);
                    }
                });
            }
        };
        new Thread(runnable).start();
        return inflate;
    }

    public class NewsRecommendRecyclerAdapter extends RecyclerView.Adapter<NewsRecommendRecyclerAdapter.ViewHolder>{

        private LayoutInflater mInflater;
        //        构造函数,只需要修改下一行的()内的参数
        public NewsRecommendRecyclerAdapter(Context context, List<NewsInfo> newsInfoList ){
            this.mInflater=LayoutInflater.from(context);
        }
        /**
         * item显示类型
         * @param parent
         * @param viewType
         * @return
         */
        @Override
//        修改R.layout后面的元素，内容为，recyelerview的子布局的layout
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=mInflater.inflate(R.layout.news_recommend_elem_layout,parent,false);
            ViewHolder viewHolder=new ViewHolder(view);
            return viewHolder;
        }
        /**
         * 数据的绑定显示
         * @param holder
         * @param position
         */
//        设置内容，和监听器。
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
//            作者名字
            holder.tv_news_name.setText( newsInfoList.get(position).getNewsName());
            holder.tv_news_authorName.setText( newsInfoList.get(position).getNewsAuthor());
            holder.tv_like_num.setText( ""+newsInfoList.get(position).getNewsLikeNum());
            holder.tv_comment_num.setText( ""+newsInfoList.get(position).getNewsCommentNum());

//          设置图片
            String url = "http://192.168.137.1:8080/news_cover/"+newsInfoList.get(position).getNewsCoverURL()+".png";
            Glide.with(getActivity())
                    .load(url)
                    .into(holder.iv_news_image);

            url = "http://192.168.137.1:8080/news_author_head/"+newsInfoList.get(position).getNewsAuthorHeadURL()+".png";
            Glide.with(getActivity())
                    .load(url)
                    .into(holder.iv_news_author);


            NewsInfo info =newsInfoList.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),NewsReadActivity.class);
                    intent.putExtra("nowNewsInfo", info);

                    startActivity(intent);
                }
            });

//            holder.iv_news_image.set( newsInfoList.get(position).getNewsCommentNum());

        }

        @Override
        public int getItemCount() {
            return newsInfoList.size();
        }

        //
        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public  class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_news_name;
            public TextView tv_news_authorName;
            public TextView tv_like_num;
            public TextView tv_comment_num;

            public ImageView iv_news_author;
            public ImageView iv_news_image;
            @SuppressLint("WrongViewCast")
            public ViewHolder(View view){
                super(view);
                tv_news_name = (TextView)view.findViewById(R.id.tv_news_name);
                tv_news_authorName=(TextView)view.findViewById(R.id.tv_news_authorName);
                tv_like_num=(TextView)view.findViewById(R.id.tv_like_num);
                tv_comment_num=(TextView)view.findViewById(R.id.tv_comment_num);
                iv_news_image = (ImageView) view.findViewById(R.id.iv_news_image);
                iv_news_author = (ImageView) view.findViewById(R.id.iv_news_author);

            }
        }
    }

}
