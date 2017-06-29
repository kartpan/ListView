package com.example.karthik.listview;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetails extends AppCompatActivity {

    public Toolbar myToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        myToolbar = (Toolbar) findViewById(R.id.newstoolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        WebView wvNewsfeed = (WebView) findViewById(R.id.wvNews);
        wvNewsfeed.loadUrl(url);
        wvNewsfeed.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                myToolbar.setTitle(view.getTitle());
            }
        });

    }
}
