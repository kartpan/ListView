package com.example.karthik.listview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NewsDetails extends AppCompatActivity {

    public Toolbar myToolbar = null;
    public ProgressBar myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_news_details);

        myToolbar = (Toolbar) findViewById(R.id.newstoolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        WebView wvNewsfeed = (WebView) findViewById(R.id.wvNews);
        wvNewsfeed.loadUrl(url);

        // request the progress-bar feature for the activity
        myProgress = (ProgressBar) findViewById(R.id.progressBar);

        // set a webChromeClient to track progress
        wvNewsfeed.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && myProgress.getVisibility() == ProgressBar.GONE) {
                    myProgress.setVisibility(ProgressBar.VISIBLE);
                }

                myProgress.setProgress(progress);
                if (progress == 100) {
                    myProgress.setVisibility(ProgressBar.GONE);
                }
            }
        });

        wvNewsfeed.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                myToolbar.setTitle(view.getTitle());
            }
        });


    }
}
