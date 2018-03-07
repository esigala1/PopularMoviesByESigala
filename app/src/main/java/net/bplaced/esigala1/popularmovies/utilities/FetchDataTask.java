package net.bplaced.esigala1.popularmovies.utilities;

import android.os.AsyncTask;
import android.util.Log;

import net.bplaced.esigala1.popularmovies.Movie;

import java.net.URL;

/**
 * Class to fetch the data from the web asynchronous extending the AsyncTask.
 *
 * Created by Effie Sigala on 7/3/2018.
 */

public class FetchDataTask extends AsyncTask<String, Integer, Movie[]> {

    /* Tag for the log messages. */
    private final String LOG_TAG = "DEBUGGING " + FetchDataTask.class.getSimpleName();

    private AsyncTaskCompleteListener<Movie[]> listener;

    public FetchDataTask(AsyncTaskCompleteListener<Movie[]> listener) {
        Log.d(LOG_TAG,"Constructor");
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        Log.d(LOG_TAG,"onPreExecute()");
        super.onPreExecute();
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
    protected void onPostExecute(Movie[] fetchedData) {
        Log.d(LOG_TAG,"onPostExecute()");

        super.onPostExecute(fetchedData);
        listener.onTaskComplete(fetchedData);
    }

    /**
     * Interface to inform a separate Activity that the outer class {@link FetchDataTask}
     * has completed its asynchronous task.
     */
    public interface AsyncTaskCompleteListener<T> {

        /**
         * Invoked when the AsyncTask has completed its execution.
         *
         * @param result The resulting object from the AsyncTask.
         */
        public void onTaskComplete(T result);
    }
}
