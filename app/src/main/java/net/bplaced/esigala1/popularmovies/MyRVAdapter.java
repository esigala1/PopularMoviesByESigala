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

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.bplaced.esigala1.popularmovies.utilities.NetworkUtils;

/**
 * {@link MyRVAdapter} exposes a list of items to a {@link android.support.v7.widget.RecyclerView}
 *
 * Created by Effie Sigala on 6/3/2018.
 */

public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.MyViewHolder> {

    /* Tag for the log messages. */
    private final String LOG_TAG = "DEBUGGING " + MyRVAdapter.class.getSimpleName();

    private Context mContext;

    private Movie[] mSourceData;

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private MyRVAdapterOnClickHandler mClickHandler;

    /**
     * The interface to handle clicks on items within this Adapter.
     */
    public interface MyRVAdapterOnClickHandler {
        /* Method to call whenever an item is clicked in the list */
        void onClickItem(Movie dataForClickedItem);
    }

    MyRVAdapter(MyRVAdapterOnClickHandler clickHandler){
        Log.d(LOG_TAG, "Constructor()");
        this.mClickHandler = clickHandler;
    }

    /**
     * onCreateViewHolder is called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent    The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new {@link MyViewHolder} that holds the View for each list item
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder()");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        /* Set the Context */
        mContext = parent.getContext();

        /*
         * If we don't want to pass the number of ViewHolders that have been created
         * into the new ViewHolder object, then we create the object and return it immediately.
         */
         return new MyViewHolder(view);
    }

    /**
     * onBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the item
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder     The ViewHolder which should be updated to represent the
     *                   contents of the item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder(): Position = " + position);
        holder.bind(position);
    }

    /**
     * Return the number of items to display.
     *
     * @return The number of items available in our list of items
     */
    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "getItemCount()");
        try{
            /* If there are no items to display, then... */
            if (mSourceData == null || mSourceData.length == 0){
                Log.d(LOG_TAG, "getItemCount(): No items to display.");
                /* Return zero length */
                return 0;
            }
            /* If there are some items to display, then... */
            Log.d(LOG_TAG, String.format("getItemCount(): There are %d items to display.", mSourceData.length));
            /* Return the length of source data */
            return mSourceData.length;
        }
        catch (NullPointerException ex){
            Log.e(LOG_TAG, "getItemCount(): NullPointerException caught: " + ex);
            /* Return zero length */
            return 0;
        }
    }

    /**
     * This method is used to set the data on an Adapter if we've already created one.
     * This is handy when we get new data from the web but don't want to create a
     * new Adapter to display it.
     *
     * @param newData The new data to be displayed.
     */
    void setData(Movie[] newData) {
        mSourceData = newData;
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a list item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* Tag for the log messages. */
        private final String LOG_TAG = "DEBUGGING " + MyViewHolder.class.getSimpleName();

        /* Variables for the item views */
        final ImageView ivPoster;

        MyViewHolder(View itemView) {
            super(itemView);
            Log.d(LOG_TAG, "Constructor()");
            /* Bind the item views variables to the item xml */
            ivPoster = itemView.findViewById(R.id.rvi_image_view_poster);
            /* Set the OnClickListener on the View passed into the constructor */
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            Log.d(LOG_TAG, "bind(): Position = " + getAdapterPosition());

            /* Get an instance of the selected object */
            Movie selectedMovie = mSourceData[listIndex];

            /* Set the URI to the ImageView */
            Picasso.with(mContext)
                    .load(NetworkUtils.buildImageURL(selectedMovie.getImageThumbnail()))
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .fit()
                    .into(ivPoster); // ImageView
        }

        /**********************************************************************************************
         * Implementation for the {@link View.OnClickListener} interface
         *********************************************************************************************/

        /**
         * Called whenever a user clicks on an item in the list.
         *
         * @param view the View that was clicked
         */
        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "onClick(): Position = " + getAdapterPosition());
            mClickHandler.onClickItem(mSourceData[getAdapterPosition()]);
        }

    }
}
