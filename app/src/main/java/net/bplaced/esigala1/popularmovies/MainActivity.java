/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bplaced.esigala1.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.bplaced.esigala1.popularmovies.utilities.NetworkUtils;
import net.bplaced.esigala1.popularmovies.utilities.TheMovieDBJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MyRVAdapter.MyRVAdapterOnClickHandler {

    /* Tag for the log messages. */
    private final String LOG_TAG = "DEBUGGING " + MainActivity.class.getSimpleName();

    /* Set integers for the number of grid columns depending on the orientation of the device */
    private static final int GRID_COLUMNS_PORTRAIT = 3;
    private static final int GRID_COLUMNS_LANDSCAPE = 5;

    /* Set strings to save the state of the activity */
    private static final String STATE_SORT_ORDER = "sort_order";

    /* Set strings for the sort order available methods */
    private static final String SORT_ORDER_MOST_POPULAR = "popularity.desc";
    private static final String SORT_ORDER_TOP_RATED = "vote_average.desc";

    private MyRVAdapter mRVAdapter;
    private RecyclerView mRecyclerView;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    /* String to keep the current sort order (Default: Most Popular) */
    private String sortOrderCurrent = SORT_ORDER_MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = findViewById(R.id.rv);

        /*
         * A GridLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a grid.
         */
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getGridColumns()));
        /*
         * The RecyclerViewAdapter is responsible for linking our data with the Views that
         * will end up displaying our data.
         */
        mRVAdapter = new MyRVAdapter(this);

        /* Set the adapter for the RecyclerView */
        mRecyclerView.setAdapter(mRVAdapter);

        /* ------------ If there is a saved state, then restore it!! ------------ */
        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "A saved instance state found...");
            sortOrderCurrent = savedInstanceState.getString(STATE_SORT_ORDER);
        }

        /* The views are setup, so load the data. */
        loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState()");
        outState.putString(STATE_SORT_ORDER, sortOrderCurrent);
    }

    /**
     * Method to get the number of grid columns depending on the orientation of the device.
     */
    public int getGridColumns(){
        Log.d(LOG_TAG, "getGridColumns()");
        /* If the WindowManager is null, then...  */
        if (((WindowManager) getSystemService(WINDOW_SERVICE)) == null) {
            Log.e(LOG_TAG, "Unable to retrieve the WindowManager for accessing the system's window manager.");
            /* Return the default number of columns. */
            return GRID_COLUMNS_PORTRAIT;
        }
        /* Get the orientation of the device */
        final int rotation = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        /* Return the number of grid columns depending on the orientation of the device */
        switch (rotation) {
            case Surface.ROTATION_0:
                Log.d(LOG_TAG, "Orientation Portrait");
                return GRID_COLUMNS_PORTRAIT;
            case Surface.ROTATION_90:
                Log.d(LOG_TAG, "Orientation Landscape");
                return GRID_COLUMNS_LANDSCAPE;
            case Surface.ROTATION_180:
                Log.d(LOG_TAG, "Orientation Reverse Portrait");
                return GRID_COLUMNS_PORTRAIT;
            default:
                Log.d(LOG_TAG, "Orientation Reverse Landscape");
                return GRID_COLUMNS_LANDSCAPE;
        }
    }

    /**
     * This method will get the user's preferred shorting option, and then tell some
     * background method to get the data in the background.
     */
    private void loadData() {
        Log.d(LOG_TAG, "loadData()");
        /* If the device does not have an internet connection, then... */
        if (!NetworkUtils.hasInternetConnection(this)){
            showErrorMessage(true);
            return;
        }
        /* Make the View for the data visible and hide the error message */
        showDataView();
        /* Retrieve the data from the web */
        new FetchDataTask().execute(sortOrderCurrent);
    }

    /**
     * This method will refresh the data.
     */
    private void refreshData(){
        Log.d(LOG_TAG, "refreshData()");
        mRVAdapter.setData(null);
        loadData();
    }

    /**
     * This method will make the View for the data visible and hide the error message.
     */
    private void showDataView() {
        Log.d(LOG_TAG, "showDataView()");
        /* First, make sure the error message is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the data View.
     */
    private void showErrorMessage(Boolean isInternetConnectionError) {
        Log.d(LOG_TAG, "showErrorMessage()");
        /* Set the text of the TextView depending on the kind of error */
        if (isInternetConnectionError){
            Log.d(LOG_TAG, "Display a no internet connection error message.");
            mErrorMessageDisplay.setText(getResources().getString(R.string.error_msg_no_internet_connection));
        } else {
            Log.d(LOG_TAG, "Display a general error message.");
            mErrorMessageDisplay.setText(getResources().getString(R.string.error_msg_general));
        }
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error message */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This method will initialize the variable depending on the preferred sort order.
     */
    private void setSortOrder(String sortOrder){
        Log.d(LOG_TAG, "setSortOrder()");
        // If the user selected the MOST POPULAR sort order, then...
        if (sortOrder.equals(SORT_ORDER_MOST_POPULAR)){
            Log.d(LOG_TAG, "#Sort Order: Most Popular");
            // Initialize the variable.
            sortOrderCurrent = SORT_ORDER_MOST_POPULAR;
        }
        // The user selected the TOP RATED sort order, so...
        else {
            Log.d(LOG_TAG, "#Sort Order: Top Rated");
            // Initialize the variable.
            sortOrderCurrent = SORT_ORDER_TOP_RATED;
        }
    }

    /**********************************************************************************************
     * Implementations regarding the Overflow menu interfaces
     *********************************************************************************************/

    /**
     * Initialize the contents of the Activity's standard options menu (overflow menu).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu()");
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Prepare the Screen's standard options menu to be displayed.
     * Note: Invoked by the method onCreateOptionsMenu() or invalidateOptionsMenu().
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onPrepareOptionsMenu()");
        // Check the corresponding menu item depending on the selected sort order.
        switch (sortOrderCurrent){
            case SORT_ORDER_MOST_POPULAR:
                // Check the menu item "Most Popular"
                menu.findItem(R.id.action_sort_most_popular).setChecked(true);
                break;
            case SORT_ORDER_TOP_RATED:
                // Check the menu item "Top Rated"
                menu.findItem(R.id.action_sort_top_rated).setChecked(true);
                break;
            default:
                Log.e(LOG_TAG, "onPrepareOptionsMenu(): No case matched.");
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected()");

        // Get the ID of the clicked item.
        int id = item.getItemId();

        switch (id){
            case R.id.action_refresh:
                // Refresh the data.
                refreshData();
                return true;
            case R.id.action_sort_most_popular:
                // Check the item
                item.setChecked(true);
                // Change the sort order.
                setSortOrder(SORT_ORDER_MOST_POPULAR);
                // Refresh the data.
                refreshData();
                return true;
            case R.id.action_sort_top_rated:
                // Check the item
                item.setChecked(true);
                // Change the sort order.
                setSortOrder(SORT_ORDER_TOP_RATED);
                // Refresh the data.
                refreshData();
                return true;
            default:
                Log.e(LOG_TAG, "onOptionsItemSelected(): No case matched.");
                // The menu item was not handled, so call the superclass implementation of
                // onOptionsItemSelected() (the default implementation returns "false").
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************
     * Implementation for the {@link MyObjectRVAdapter.MyObjectRVAdapterOnClickHandler} interface
     *********************************************************************************************/

    /**
     * This callback is invoked when you click on an item in the list.
     *
     * @param dataForClickedItem  Info for the movie that was clicked
     */
    @Override
    public void onClickItem(Movie dataForClickedItem) {
        Log.d(LOG_TAG,"onClickItem()");

        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        /* Add extended data to the intent */
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, dataForClickedItem);
        startActivity(intentToStartDetailActivity);
    }

    public class FetchDataTask extends AsyncTask<String, Void, Movie[]> {

        /* Tag for the log messages. */
        private final String LOG_TAG = "DEBUGGING " + FetchDataTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG,"onPreExecute()");
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            Log.d(LOG_TAG,"doInBackground()");

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            /* The preferred sort order passed through the parameters of the task as a String */
            String sortOrder = params[0];

            /* The URL to request the data */
            URL dataRequestUrl = NetworkUtils.buildUrl(sortOrder);

            try {
                /* Get the entire result from the HTTP response */
                String jsonDataResponse = NetworkUtils.getResponseFromHttpUrl(dataRequestUrl);

                /* Return an array of Strings containing info for the movies */
                return TheMovieDBJsonUtils.getDataFromJson(jsonDataResponse);

            } catch (Exception ex) {
                Log.e(LOG_TAG,"doInBackground(): Exception caught: " + ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] retrievedData) {
            Log.d(LOG_TAG,"onPostExecute()");
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (retrievedData != null) {
                showDataView();
                mRVAdapter.setData(retrievedData);
            } else {
                showErrorMessage(false);
            }
        }
    }
}
