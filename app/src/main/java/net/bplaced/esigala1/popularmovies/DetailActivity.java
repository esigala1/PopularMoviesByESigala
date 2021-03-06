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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.bplaced.esigala1.popularmovies.model.Movie;
import net.bplaced.esigala1.popularmovies.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    /* Tag for the log messages. */
    private final String LOG_TAG = "DEBUGGING " + DetailActivity.class.getSimpleName();

    @BindView(R.id.iv_poster) ImageView ivPoster;
    @BindView(R.id.tv_original_title) TextView tvOriginalTitle;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.tv_overview) TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* Initialize Butter Knife and Bind the Views to the Activity */
        ButterKnife.bind(this);

        /* Get the intent that started this activity. */
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            /* If the intent has extended data associated with the given key name, then... */
            if (intentThatStartedThisActivity.hasExtra(MainActivity.KEY_PARCELABLE_DATA)) {
                /* Retrieve extended data (parcelable data) from the intent */
                Movie mItemData = (Movie) intentThatStartedThisActivity.getParcelableExtra(MainActivity.KEY_PARCELABLE_DATA);
                /* Set the data to the corresponding views */
                tvOriginalTitle.setText(getResources().getString(R.string.item_original_title, mItemData.getOriginalTitle()));
                tvReleaseDate.setText(getResources().getString(R.string.item_release_date, mItemData.getReleaseDate()));
                tvRating.setText(getResources().getString(R.string.item_rating, mItemData.getRating()));
                tvOverview.setText(getResources().getString(R.string.item_overview, mItemData.getOverview()));
                /* Set the URI to the ImageView */
                Picasso.with(this)
                        .load(NetworkUtils.buildImageURL(mItemData.getImageThumbnail()))
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .fit()
                        .into(ivPoster); // ImageView
            }
        }
    }
}
