package com.example.karthik.listview;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "";
    public SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        loadData();

        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadData();
                        swipe.setRefreshing(false);
                    }
                }
        );

    }

    public void loadData() {

        NewsDBHelper newsDB = new NewsDBHelper(this);

        ListView listView = (ListView) findViewById(R.id.listView);

        List<NewsFeed> listNewsFeed = new ArrayList<NewsFeed>();

        Cursor newsCursor = newsDB.getAllNews();

        List<NewsArticles> listArticles = new ArrayList<NewsArticles>();

        try {
            while (newsCursor.moveToNext()) {

                listArticles.add(new NewsArticles(newsCursor.getString(newsCursor.getColumnIndex("title")),
                        newsCursor.getString(newsCursor.getColumnIndex("subtitle")),
                        newsCursor.getString(newsCursor.getColumnIndex("url")),
                        newsCursor.getString(newsCursor.getColumnIndex("imageurl"))));

            }
        } finally {
            newsCursor.close();
        }

        final String[] arrayUrl = new String[listArticles.size()];

        int count = 0;


        for (NewsArticles articles : listArticles) {
            try {

                URL url = new URL(articles.getImageURL());
                listNewsFeed.add(new NewsFeed(
                        articles.getImageURL(),
                        articles.getTitle(),
                        articles.getSubTitle()));
                arrayUrl[count++] = articles.getURL();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        NewsFeedAdaptor adapter = new NewsFeedAdaptor(this, listNewsFeed);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(view.getContext(), NewsDetails.class);
                String url = arrayUrl[i];
                intent.putExtra(EXTRA_MESSAGE, url);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load:
                NewsDBHelper newsDB = new NewsDBHelper(this);

                newsDB.deleteAllNews();

                newsDB.insertNews(
                        "Late British couple who left $6m to Assisi Hospice, NKF, SPCA became" +
                                " Singaporeans in the 1970s",
                        "A generous couple, the late Mr Gerry Essery and Mrs Jo Essery, has left " +
                                "behind a $6 million legacy that will be divided equally among the Assisi " +
                                "Hospice, National Kidney Foundation (NKF) and the Society for the " +
                                "Prevention of Cruelty to Animals (SPCA).",
                        "http://www.straitstimes.com/singapore/generous-couple-leaves-6-million-donation-to-assisi-hospice-nkf-and-spca",
                        "http://www.straitstimes.com/sites/default/files/styles/article_pictrure_780x520_/public/articles/2017/06/29/jo_gerry_3.jpg?itok=52aSb55g");

                newsDB.insertNews(
                        "PropertyGuru's Melhuish doesn't believe in 'unicorns' even as portal " +
                                "nears $1b mark",
                        "In fact, when it comes to the tech scene, Melhuish, who stepped down as chief" +
                                " executive of the realty portal late last year, handing over day-to-day " +
                                "operations to Hari V. Krishnan, says ‘Unicorn is among his least favourite " +
                                "words’ to describe successful startups.",
                        "https://www.dealstreetasia.com/stories/we-are-close-to-hitting-1b-valuation-mark-steve-melhuish-propertyguru-75995/",
                        "http://cdn.dealstreetasia.com/uploads/2017/06/Steve-Melhuish.jpg?resize=750,417");

                newsDB.insertNews(
                        "Vietnam: ESP Capital invests in women-focused careers platform Canavi",
                        "ESP Capital, the new homegrown venture capital firm in Vietnam, " +
                                "has invested in Canavi, a recruitment platform for women.",
                        "https://www.dealstreetasia.com/stories/vietnam-esp-capital-women-career-canavi-76629/",
                        "http://cdn.dealstreetasia.com/uploads/2017/07/canavi.jpg?resize=750,417");


                newsDB.insertNews(
                        "Makansutra: Big Lazy Chop boasts excellent zi char",
                        "Zi char meals are the greatest family makan institution in Singapore. " +
                                "This is where families head to when everyone cannot agree on what " +
                                "to eat at weekend gatherings.",
                        "http://www.tnp.sg/lifestyle/makan/makansutra-big-lazy-chop-boasts-excellent-zi-char",
                        "http://www.tnp.sg/sites/default/files/styles/rl780/public/articles/2017/06/29/np_20170629_makan29_1580315.jpg?itok=8xN3UXgL");


                Toast.makeText(this, "Loaded data to DB", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.action_search:
                Toast.makeText(this,"Search menu",Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
