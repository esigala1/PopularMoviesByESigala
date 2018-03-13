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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * These utilities will be used to communicate with the "themoviedb.org" servers.
 *
 */
public final class NetworkUtils {

    /* Tag for the log messages. */
    private static final String LOG_TAG = "DEBUGGING " + NetworkUtils.class.getSimpleName();

    /* The TheMovieDB Image Size value */
    private final static String IMAGE_SIZE = "w185";

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

    /**
     * Method to determine if the device has Internet Connection (Network Availability).
     */
    public static boolean hasInternetConnection(Context context){
        /* Use the ConnectivityManager to check if the device is actually connected to the Internet */
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            /* Use getActiveNetworkInfo() to get an instance that represents the current network connection */
            activeNetwork = cm.getActiveNetworkInfo();
        }
        /* Return the state of connection */
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}