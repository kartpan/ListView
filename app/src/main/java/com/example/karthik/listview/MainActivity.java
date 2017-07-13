package com.example.karthik.listview;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        SwipeActionAdapter.SwipeActionListener,
        SearchView.OnQueryTextListener {

    public static final String EXTRA_MESSAGE = "";
    public SwipeRefreshLayout swipe;
    protected SwipeActionAdapter swipeAdapter;
    protected List<NewsArticles> listArticles;
    protected List<Integer> positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        positions = new ArrayList<Integer>();

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

        final ListView listView = (ListView) findViewById(R.id.listView);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);


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

        ImageView nodata = (ImageView) findViewById(R.id.nodataview);
        LinearLayout content = (LinearLayout) findViewById(R.id.listlayout);

        if (listArticles.size() == 0) {

            nodata.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        } else {

            nodata.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            final NewsFeedAdaptor adapter = new NewsFeedAdaptor(this, listArticles);
            swipeAdapter = new SwipeActionAdapter(adapter);
            swipeAdapter.setSwipeActionListener(this)
                    .setDimBackgrounds(true)
                    .setListView(listView);
            listView.setAdapter(swipeAdapter);

            // Set backgrounds for the swipe directions
            swipeAdapter.addBackground(SwipeDirection.DIRECTION_FAR_LEFT, R.layout.row_bg_left)
                    .addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.row_bg_left);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(view.getContext(), NewsDetails.class);
                    String url = listArticles.get(i).getURL();
                    intent.putExtra(EXTRA_MESSAGE, url);
                    startActivity(intent);
                }
            });


            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    //editListAdapter.removeSelection();

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.delete, menu);
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.delete_mode:
                            for (Iterator<Integer> iter = positions.listIterator(); iter.hasNext(); ) {
                                int currentPosition = iter.next();
                                deleteRecord(listArticles.get(currentPosition).getId());
                            }
                            loadData();
                            mode.finish();
                            positions.clear();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                                      boolean checked) {
                    int checkedCount = listView.getCheckedItemCount();
                    positions.add(position);
                    listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.colorListSelectedBG));

                    if (!checked) {

                        for (Iterator<Integer> iter = positions.listIterator(); iter.hasNext(); ) {
                            int a = iter.next();
                            if (a == position) {
                                listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.colorListBG));
                                iter.remove();
                            }
                        }
                    }
                    mode.setTitle(checkedCount + " selected");
                }
            });


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load:
                loadDatafromDB();
                return true;

            case R.id.action_search:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void loadDatafromDB() {

        NewsDBHelper newsDB = new NewsDBHelper(this);

        newsDB.deleteAllNews();

        String[] title = getResources().getStringArray(R.array.title);
        String[] description = getResources().getStringArray(R.array.description);
        String[] image_url = getResources().getStringArray(R.array.image_url);
        String[] url = getResources().getStringArray(R.array.url);

        for (int count = 0; count < title.length; count++) {

            newsDB.insertNews(title[count], description[count], url[count], image_url[count]);

        }

        final Context context = this;

        View parentLayout = findViewById(R.id.rootView);

        Snackbar.make(parentLayout, "Loading completed...", Snackbar.LENGTH_LONG)
                .setAction("CLEAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Delete file
                        NewsDBHelper newsDB = new NewsDBHelper(context);
                        newsDB.deleteAllNews();
                        Toast.makeText(context, "Cleared data ...", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .show();
        loadData();
    }

    // Methods from SearchView.OnQueryTextListener

    @Override
    public boolean onQueryTextSubmit(String query) {

        Intent intent = new Intent(this, SearchListing.class);
        intent.putExtra(EXTRA_MESSAGE, query);
        startActivity(intent);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // This method can be used to dynamically referesh the list view
        return true;
    }

    // Methods from SwipeActionAdapter.SwipeActionListener

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
