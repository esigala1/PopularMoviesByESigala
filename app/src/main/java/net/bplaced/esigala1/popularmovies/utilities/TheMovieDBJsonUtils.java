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
package net.bplaced.esigala1.popularmovies.utilities;

import android.util.Log;

import net.bplaced.esigala1.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle TheMovieDB JSON data.
 */
public final class TheMovieDBJsonUtils {

    /* Tag for the log messages. */
    private static final String LOG_TAG = "DEBUGGING " + TheMovieDBJsonUtils.class.getSimpleName();

    /**
     * This method parses JSON from a web response and returns an array of Movies
     * describing the requested data.
     *
     * @param dataJsonStr JSON response from server.
     *
     * @return Array of Strings describing the data.
     *
     * @throws JSONException If JSON data cannot be properly parsed.
     */
    public static Movie[] getDataFromJson(String dataJsonStr) throws JSONException {
        Log.d(LOG_TAG, "getDataFromJson()");

        final String MOVIEDB_TOTAL_RESULTS = "total_results";

        final String MOVIEDB_RESULTS = "results";

        /* Weather information. Each day's forecast info is an element of the "list" array */
        final String MOVIEDB_ORIGINAL_TITLE = "original_title";
        final String MOVIEDB_IMAGE_THUMBNAIL = "poster_path";
        final String MOVIEDB_OVERVIEW = "overview";
        final String MOVIEDB_RATING = "vote_average";
        final String MOVIEDB_RELEASE_DATE = "release_date";

        /* Get the JSON object representing the parent node */
        JSONObject dataJson = new JSONObject(dataJsonStr);

        /* If the json key "total_results" does not exist, then... */
        if (dataJson.has(MOVIEDB_TOTAL_RESULTS)) {
            /* Extract out the total results of this query */
            int totalResults = dataJson.getInt(MOVIEDB_TOTAL_RESULTS);
            if (totalResults <= 0){
                Log.e(LOG_TAG, "No results found.");
                return null;
            }
        }

        /* Get a list of movies from the JSON object */
        JSONArray resultsArray = dataJson.getJSONArray(MOVIEDB_RESULTS);

        /* Declare and initialize an array of Movies to hold each movie's info */
        Movie[] parsedResultsData = new Movie[resultsArray.length()];

        /* Add each movies into the array of Movies */
        for (int i = 0; i < resultsArray.length(); i++) {

            /* Declare the values that will be collected */
            String originalTitle;
            String imageThumbnail;
            String overview;
            Double rating;
            String releaseDate;

            /* Get the JSON object representing the movie */
            JSONObject movie = resultsArray.getJSONObject(i);

            /* Extract out the original title of this movie */
            originalTitle = movie.optString(MOVIEDB_ORIGINAL_TITLE);

            /* Extract out the movie poster image thumbnail */
            imageThumbnail = movie.optString(MOVIEDB_IMAGE_THUMBNAIL);

            /* Extract out the plot synopsis (overview) */
            overview = movie.optString(MOVIEDB_OVERVIEW);

            /* Extract out the user rating */
            rating = movie.optDouble(MOVIEDB_RATING);

            /* Extract out the release date */
            releaseDate = movie.optString(MOVIEDB_RELEASE_DATE);

            /* Initialize the Movie object */
            parsedResultsData[i] = new Movie();

            /* Set the values to the object */
            parsedResultsData[i].setOriginalTitle(originalTitle);
            parsedResultsData[i].setImageThumbnail(imageThumbnail);
            parsedResultsData[i].setOverview(overview);
            parsedResultsData[i].setRating(rating);
            parsedResultsData[i].setReleaseDate(releaseDate);
        }

        /* Return the array of Movies */
        return parsedResultsData;
    }
}