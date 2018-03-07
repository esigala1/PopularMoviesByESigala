package net.bplaced.esigala1.popularmovies;

import java.io.Serializable;

/**
 * A class to represent a single {@link Movie} item.
 *
 * Important: Implement the java.io.Serializable interface to be able to serialize/deserialize
 * its state. Useful to pass its instance through intents.
 *
 * Created by Effie Sigala on 07/03/2018.
 */

public class Movie implements Serializable {

    private String originalTitle;
    private String imageThumbnail;
    private String overview;
    private Double rating;
    private String releaseDate;

    /* Constructor */
    public Movie(){

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
}
