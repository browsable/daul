package com.daemin.community.amazon;

/**
 * Created by hernia on 2015-06-20.
 */


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.daemin.common.MyVolley;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.SimpleXmlRequest;


public class AmazonActivity extends Activity {
    private static final String GET_ITEMS_URL = "http://www.amazon.com/rss/tag/running/recent/ref=tag_rsh_hl_ersr";

    private RequestQueue requestQueue;
    private ImageLoader loader;
    private ListView listView;
    private ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amazon);

        listView = (ListView) findViewById(R.id.listView);
        loadingProgress = (ProgressBar) findViewById(R.id.loadingProgress);

        loader = MyVolley.getImageLoader();
        requestQueue = MyVolley.getRequestQueue();

        loadShoppingItems();
    }

    private void loadShoppingItems() {
        SimpleXmlRequest<ShoppingRssFeed> request = new SimpleXmlRequest<ShoppingRssFeed>(
                GET_ITEMS_URL, ShoppingRssFeed.class,
                new Listener<ShoppingRssFeed>() {

                    @Override
                    public void onResponse(final ShoppingRssFeed feed) {
                        // Hide a loading bar, and show a list view
                        loadingProgress.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);

                        listView.setAdapter(new AmazonListAdapter(AmazonActivity.this, feed.getChannel().getShoppingItems()));
                    }
                });

        requestQueue.add(request);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}