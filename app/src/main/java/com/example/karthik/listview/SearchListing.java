package com.example.karthik.listview;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchListing extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "";
    ArrayList<NewsArticles> listArticles;
    public Toolbar myToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_listing);

        myToolbar = (Toolbar) findViewById(R.id.newstoolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadData();

    }

    public void loadData() {

        NewsDBHelper newsDB = new NewsDBHelper(this);

        ListView listView = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        String query = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Cursor newsCursor = newsDB.searchNews(query);

        listArticles = new ArrayList<NewsArticles>();

        try {
            while (newsCursor.moveToNext()) {

                listArticles.add(new NewsArticles(
                        newsCursor.getInt(newsCursor.getColumnIndex("id")),
                        newsCursor.getString(newsCursor.getColumnIndex("title")),
                        newsCursor.getString(newsCursor.getColumnIndex("subtitle")),
                        newsCursor.getString(newsCursor.getColumnIndex("url")),
                        newsCursor.getString(newsCursor.getColumnIndex("imageurl"))));

            }
        } finally {
            newsCursor.close();
        }

        ImageView nodata = (ImageView) findViewById(R.id.nodataview);
        ListView content = (ListView) findViewById(R.id.listView);

        if (listArticles.size() == 0) {

            nodata.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        } else {
            nodata.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            NewsFeedAdaptor adapter = new NewsFeedAdaptor(this, listArticles);
            listView.setAdapter(adapter);

            //listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(view.getContext(), NewsDetails.class);
                    String url = listArticles.get(i).getURL();
                    intent.putExtra(EXTRA_MESSAGE, url);
                    startActivity(intent);
                }
            });
        }

    }
}
