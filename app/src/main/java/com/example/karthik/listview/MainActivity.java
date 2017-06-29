package com.example.karthik.listview;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ListView listView = (ListView) findViewById(R.id.listView);

        List<NewsFeed> listNewsFeed = new ArrayList<NewsFeed>();
        listNewsFeed.add(new NewsFeed(
                BitmapFactory.decodeResource(getResources(), R.drawable.article1),
                "Late British couple who left $6m to Assisi Hospice, NKF, SPCA became" +
                        " Singaporeans in the 1970s",
                "A generous couple, the late Mr Gerry Essery and Mrs Jo Essery, has left " +
                        "behind a $6 million legacy that will be divided equally among the Assisi " +
                        "Hospice, National Kidney Foundation (NKF) and the Society for the " +
                        "Prevention of Cruelty to Animals (SPCA)."));
        listNewsFeed.add(new NewsFeed(
                BitmapFactory.decodeResource(getResources(), R.drawable.article2),
                "Makansutra: Big Lazy Chop boasts excellent zi char",
                "Zi char meals are the greatest family makan institution in Singapore. " +
                        "This is where families head to when everyone cannot agree on what " +
                        "to eat at weekend gatherings."));
        listNewsFeed.add(new NewsFeed(
                BitmapFactory.decodeResource(getResources(), R.drawable.article3),
                "PropertyGuru's Melhuish doesn't believe in 'unicorns' even as portal " +
                        "nears $1b mark",
                "In fact, when it comes to the tech scene, Melhuish, who stepped down as chief" +
                        " executive of the realty portal late last year, handing over day-to-day " +
                        "operations to Hari V. Krishnan, says ‘Unicorn is among his least favourite " +
                        "words’ to describe successful startups."));
        NewsFeedAdaptor adapter = new NewsFeedAdaptor(this, listNewsFeed);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(view.getContext(), NewsDetails.class);
                String url = "";
                switch (i){
                    case 0:
                        url = "http://www.straitstimes.com/singapore/generous-couple-leaves-6-million-donation-to-assisi-hospice-nkf-and-spca";
                        break;

                    case 1:
                        url = "http://www.tnp.sg/lifestyle/makan/makansutra-big-lazy-chop-boasts-excellent-zi-char";
                        break;

                    case 2:
                        url = "https://www.dealstreetasia.com/stories/we-are-close-to-hitting-1b-valuation-mark-steve-melhuish-propertyguru-75995/";
                        break;

                    default:
                        url = "http://www.straitstimes.com/singapore/generous-couple-leaves-6-million-donation-to-assisi-hospice-nkf-and-spca";
                        break;
                }
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
            case R.id.action_settings:
                Toast.makeText(this,"Settings menu",Toast.LENGTH_SHORT).show();
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
