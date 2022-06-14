package com.example.dmzj;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class htmlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
//        htmlTextView.setClickable(true);
//        htmlTextView.setMovementMethod(LinkMovementMethod.getInstance());

        HtmlTextView htmlTextView = (HtmlTextView) this.findViewById(R.id.html_text);
        String html_url="<h2>Hello wold</h2><img src=\"https://images.cnblogs.com/cnblogs_com/Gealach/1632018/o_200113094540qq.jpg\"/>";
        htmlTextView.setHtml(html_url,
                new HtmlHttpImageGetter(htmlTextView));
    }

}