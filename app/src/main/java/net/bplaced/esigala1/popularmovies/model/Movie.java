package net.bplaced.esigala1.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * A class to represent a single {@link Movie} item.
 *
 * Important: Implement the Parcelable interface to be able to pass an instance
 * of this object between activities within an intent.
 *
 * Created by Effie Sigala on 07/03/2018.
 */

public class Movie implements Parcelable {

    /* Tag for the log messages. */
    private final String LOG_TAG = "DEBUGGING " + Movie.class.getSimpleName();

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("poster_path")
    private String imageThumbnail;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private Double rating;

    @SerializedName("release_date")
    private String releaseDate;

    /* Default Constructor */
    public Movie(){
        Log.d(LOG_TAG, "Constructor ~ Default");
    }

    /**
     * Overloaded Constructor, to retrieve the values that we originally wrote into the "Parcel".
     * This constructor is usually private so that only the "CREATOR" field can access.
     *
     * @param in The Parcel in which the object has been written..
     */
    private Movie(Parcel in) {
        Log.d(LOG_TAG, "Constructor ~ Parcelable");
        originalTitle = in.readString();
        imageThumbnail = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**********************************************************************************************
     * Implementation for the {@link Parcelable} interface
     *********************************************************************************************/

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled
     * representation.
     *
     * @return a bitmask indicator.
     */
    @Override
    public int describeContents() {
        Log.d(LOG_TAG, "describeContents()");
        return 0;
    }

    /**
     * Write the values to save to the "Parcel"
     *
     * @param out    The Parcel in which the object should be written.
     * @param flags  Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        Log.d(LOG_TAG, "writeToParcel()");
        out.writeString(originalTitle);
        out.writeString(imageThumbnail);
        out.writeString(overview);
        out.writeDouble(rating);
        out.writeString(releaseDate);
    }

    /**
     * After implementing the "Parcelable" interface, create a non-null static field called CREATOR
     * of a type that implements the Parcelable.Creator interface.
     */
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        /* Tag for the log messages. */
        private final String LOG_TAG = "DEBUGGING " + Creator.class.getSimpleName();

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Movie createFromParcel(Parcel in) {
            Log.d(LOG_TAG, "createFromParcel()");
            return new Movie(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Movie[] newArray(int size) {
            Log.d(LOG_TAG, "newArray()");
            return new Movie[size];
        }
    };
}
