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

import android.net.Uri;
import android.util.Log;

import net.bplaced.esigala1.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the "themoviedb.org" servers.
 *
 * URL EXAMPLE: https://api.themoviedb.org/3/discover/movie?api_key=###&sort_by=popularity.desc&page=1
 */
public final class NetworkUtils {

    /* Tag for the log messages. */
    private static final String LOG_TAG = "DEBUGGING " + NetworkUtils.class.getSimpleName();

    /* Base URL for data from the "themoviedb.org" */
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";

    /* The TheMovieDB API Key parameter */
    private final static String API_KEY_PARAM = "api_key";

    /* The TheMovieDB Sort Order parameter */
    private final static String SORT_ORDER_PARAM = "sort_by";

    /* The TheMovieDB Image Size value */
    private final static String IMAGE_SIZE = "w185";

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param sortOrder The sorting order of the posters that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String sortOrder) {
        Log.d(LOG_TAG, "buildUrl()");

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THEMOVIEDB_ORG_API_KEY)
                .appendQueryParameter(SORT_ORDER_PARAM, sortOrder)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, "Built URI " + url);
            return url;
        } catch (MalformedURLException ex) {
            Log.e(LOG_TAG, "buildUrl(): Exception caught: " + ex);
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.d(LOG_TAG, "getResponseFromHttpUrl()");

        // If the URL is null, then return early.
        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            // If the request was not successful (response code different from 200), then...
            if (urlConnection.getResponseCode() != 200) {
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * Method to build the image URL as a String.
     *
     * @param imageName is the name of the image
     * @return the path to the image as a String
     */
    public static String buildImageURL(String imageName){
        Log.d(LOG_TAG, "buildImageURL()");
        if (imageName == null) {
            return null;
        }
        /* The base image URL */
        String baseImageURL = "http://image.tmdb.org/t/p/";
        /* Build the image URL */
        StringBuilder sb = new StringBuilder();
        sb.append(baseImageURL);
        sb.append(IMAGE_SIZE);
        sb.append("/");
        sb.append(imageName);
        Log.d(LOG_TAG, "Image URL: " + sb);
        /* Return  */
        return sb.toString();
    }
}