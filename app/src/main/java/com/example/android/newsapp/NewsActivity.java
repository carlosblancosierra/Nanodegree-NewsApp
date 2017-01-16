package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderCallbacks<List<News>> {


    /** URL for news data from the guardian api dataset */
    private String BASE_REQUEST_URL = "http://content.guardianapis.com/search?";

    /** Adapter for the list of news */
    private NewsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        /** Create a new adapter that takes an empty list of earthquakes as input */
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        /** Find a reference to the {@link ListView} in the layout */
        ListView listView = (ListView) findViewById(R.id.news_list_view);


        /** Set the adapter on the {@link ListView}
         * so the list can be populated in the user interface */
        listView.setAdapter(mAdapter);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader.
        loaderManager.initLoader(1, null, this);

        /**
         * Set the OnItemCLickListener on each item to set an Intent to the item webUrl
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNew = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNew.getWebUrl());
                Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(newsIntent);
            }
        });
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String section = sharedPreferences.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default)
        );

        String orderBy = sharedPreferences.getString(
                getString(R.string.order_by_key),
                getString(R.string.order_by_default)
        );

        Uri baseUri = Uri.parse(BASE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", section);
        uriBuilder.appendQueryParameter("api-key", "a2c9aae6-355d-4390-bb65-d3c281ee485d");
        uriBuilder.appendQueryParameter("order-by", orderBy);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
