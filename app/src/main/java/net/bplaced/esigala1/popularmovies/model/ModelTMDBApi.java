package net.bplaced.esigala1.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO that matches the structure of The Movie Database Api response.
 *
 * Created by Effie Sigala on 12/3/2018.
 */

public class ModelTMDBApi {

    /**
     * The "Gson" library, that is used by Retrofit, takes a POJO and turns is into valid JSON,
     * and vice versa. For this reason, each field in the POJO must match the name of the JSON
     * property it belongs to. If we want to name something differently, then we can annotate the
     * field with @SerializedName to tell Gson what property from the JSON to match this field with.
     */
    @SerializedName("results")
    public List<Movie> listOfMovies = new ArrayList<>();
}
