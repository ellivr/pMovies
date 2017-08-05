package com.example.android.pmovies.tools;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ref on 7/2/2017.
 */

public class Movie implements Parcelable {

//    id
//    original title
//    movie poster image thumbnail
//    A plot synopsis (called overview in the api)
//    user rating (called vote_average in the api)
//    release date
//    Available size w92, w154, w185, w342, w500, w780


    String id;
    String title;
    String overview;
    String poster_path;
    String vote_average;
    String release_date;

    public Movie(String id, String title, String overview, String poster_path, String vote_average, String release_date) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.id = id;
        this.title = title;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        title = in.readString();
        vote_average = in.readString();
        id = in.readString();
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return title;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public String getImageFullURL() {
        return GlobalVar.Const.IMAGE_BASE_URL + GlobalVar.Const.IMAGE_SMALL_SIZE + getPosterPath();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(title);
        dest.writeString(vote_average);
        dest.writeString(id);
    }
}

