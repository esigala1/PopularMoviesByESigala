package net.bplaced.esigala1.popularmovies.utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.bplaced.esigala1.popularmovies.BuildConfig;
import net.bplaced.esigala1.popularmovies.MainActivity;
import net.bplaced.esigala1.popularmovies.model.ModelTMDBApi;
import net.bplaced.esigala1.popularmovies.model.Movie;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Class to create the Retrofit client, call The Movie Database API and handle the result.
 *
 * URL EXAMPLES:
 * https://api.themoviedb.org/3/movie/popular?api_key=###&page=1
 * https://api.themoviedb.org/3/movie/top_rated?api_key=###&page=1
 *
 * Created by Effie Sigala on 12/3/2018.
 */

public class TMDBApi implements Callback<ModelTMDBApi> {

    /* Tag for the log messages. */
    private final String LOG_TAG = "DEBUGGING " + TMDBApi.class.getSimpleName();

    /* Set strings for the sort order available methods */
    public static final String SORT_ORDER_MOST_POPULAR = "popular";
    public static final String SORT_ORDER_TOP_RATED = "top_rated";

    /* Base URL for data from the "themoviedb.org" */
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    /* The TheMovieDB API Key parameter */
    private static final String API_KEY_PARAM = "api_key";

    /* Reference to listener to update the UI of {@link MainActivity} when the response is completed. */
    private AsyncTaskCompleteListener<Movie[]> listener;

    /**
     * Constructor
     *
     * @param listener to update the UI of {@link MainActivity} when the response is completed.
     */
    public TMDBApi(AsyncTaskCompleteListener<Movie[]> listener) {
        Log.d(LOG_TAG,"Constructor");
        this.listener = listener;
    }

    /**
     * Method to start the network transactions.
     */
    public void start(String sortOrderCurrent) {
        Log.d(LOG_TAG, "start()");

        /* The JSON Converter to add to Retrofit */
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        /* Set the Retrofit */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        /* Get an instance of the API Interface */
        TMDBApiInterface apiInterface = retrofit.create(TMDBApiInterface.class);

        /* Query parameters to append to the URL */
        Map<String, String> data = new HashMap<>();
        data.put(API_KEY_PARAM, BuildConfig.THEMOVIEDB_ORG_API_KEY);

        /* Get an instance of the API interface */
        Call<ModelTMDBApi> call = apiInterface.getPopularMovies(sortOrderCurrent, data);
        /* Asynchronously send the request to the webserver and notify callback of its response. */
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<ModelTMDBApi> call, @NonNull Response<ModelTMDBApi> response) {
        Log.d(LOG_TAG, "onResponse(): Response Code = " + response.code()
                + " ~ URL = " + response.raw().request().url());
        /* If the response is successful, then... */
        if(response.isSuccessful()) {
            Log.d(LOG_TAG, "onResponse(): Successful response.");

            /* Map the Model class to the response */
            ModelTMDBApi model = response.body();

            /* If the Model was initialized successfully, then... */
            if (model != null) {
                Log.d(LOG_TAG, "Number of movies = " + model.listOfMovies.size());
                /* Convert the List of Objects to an Array of Objects */
                Movie[] arrayOfMovies = model.listOfMovies.toArray(new Movie[model.listOfMovies.size()]);
                /* Inform the MainActivity that the response is completed */
                listener.onTaskComplete(arrayOfMovies);
            }
            /* The Model was not initialized successfully, so... */
            else {
                Log.e(LOG_TAG, "onResponse(): The model is null.");
                /* Inform the MainActivity that the response is completed */
                listener.onTaskComplete(null);
            }
        }
        /* The response is unsuccessful, so... */
        else {
            Log.e(LOG_TAG, "onResponse(): Unsuccessful response.");
            /* Inform the MainActivity that the response is completed */
            listener.onTaskComplete(null);
        }
    }

    @Override
    public void onFailure(@NonNull Call<ModelTMDBApi> call, @NonNull Throwable t) {
        Log.e(LOG_TAG, "onFailure(): " + t.toString());
        /* Inform the MainActivity that the response is completed */
        listener.onTaskComplete(null);
    }

    /**
     * An Interface to represent The Movie Database API.
     *
     * Note: Use Retrofit to turn a REST API into a Java interface.
     *
     * Each method of this interface will be used for network transactions, corresponding to
     * an endpoint of the REST API.
     */
    private interface TMDBApiInterface {
        /**
         * Method to make a @GET request and connect to the given endpoint.
         */
        @GET("movie/{sort_order}")
        Call<ModelTMDBApi> getPopularMovies(@Path("sort_order") String sort_order, @QueryMap Map<String, String> query_parameters);
    }

    /**
     * Interface to inform a separate Activity that the outer class {@link TMDBApi}
     * has completed its asynchronous task.
     */
    public interface AsyncTaskCompleteListener<T> {
        /**
         * Invoked when the AsyncTask has completed its execution.
         *
         * @param result The resulting object from the AsyncTask.
         */
        void onTaskComplete(T result);
    }
}
