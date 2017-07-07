package com.example.karthik.listview;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        SwipeActionAdapter.SwipeActionListener {

    public static final String EXTRA_MESSAGE = "";
    public SwipeRefreshLayout swipe;
    protected SwipeActionAdapter swipeAdapter;
    protected List<NewsArticles> listArticles;

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


        Cursor newsCursor = newsDB.getAllNews();

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

        NewsFeedAdaptor adapter = new NewsFeedAdaptor(this, listArticles);
        swipeAdapter = new SwipeActionAdapter(adapter);
        swipeAdapter.setSwipeActionListener(this)
                .setDimBackgrounds(true)
                .setListView(listView);
        listView.setAdapter(swipeAdapter);

        // Set backgrounds for the swipe directions
        swipeAdapter.addBackground(SwipeDirection.DIRECTION_FAR_LEFT, R.layout.row_bg_left)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.row_bg_left);


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

                String[] title = getResources().getStringArray(R.array.title);
                String[] description = getResources().getStringArray(R.array.description);
                String[] image_url = getResources().getStringArray(R.array.image_url);
                String[] url = getResources().getStringArray(R.array.url);


                for (int count = 0; count < title.length; count++) {

                    newsDB.insertNews(title[count], description[count], url[count], image_url[count]);

                }

                Toast.makeText(this, "Loaded data to DB", Toast.LENGTH_SHORT).show();
                loadData();

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

    @Override
    public boolean hasActions(int position, SwipeDirection direction) {
        return direction.isLeft();
    }

    @Override
    public boolean shouldDismiss(int position, SwipeDirection direction) {

        Log.i("Test:", position + "position");
        return direction == SwipeDirection.DIRECTION_FAR_LEFT;
    }

    @Override
    public void onSwipe(int[] positionList, SwipeDirection[] directionList) {
        for (int i = 0; i < positionList.length; i++) {
            SwipeDirection direction = directionList[i];
            int position = positionList[i];

            switch (direction) {
                case DIRECTION_FAR_LEFT:
                    deleteRecord(listArticles.get(position).getId());
                    break;
                case DIRECTION_NORMAL_LEFT:
                    deleteRecord(listArticles.get(position).getId());
                    break;
            }
            loadData();
        }
    }

    public void deleteRecord(int position) {

        //Delete from data base

        NewsDBHelper newsDB = new NewsDBHelper(this);
        newsDB.deleteNews(position);

    }

}
