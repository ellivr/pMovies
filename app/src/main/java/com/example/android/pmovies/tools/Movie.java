package com.example.android.pmovies.tools;

/**
 * Created by ref on 7/2/2017.
 */

public class Movie {
//    original title
//    movie poster image thumbnail
//    A plot synopsis (called overview in the api)
//    user rating (called vote_average in the api)
//    release date
    String pTitle;
    String pImagePath;
    String pSynopsis;
    String pUserRating;
    String pReleaseDate;

    public Movie(String title, String imagePath, String synopsis, String userRating, String releaseDate){
        pTitle = title;
        pImagePath = imagePath;
        pSynopsis = synopsis;
        pUserRating = userRating;
        pReleaseDate = releaseDate;
    }

    public String getImagePath(ImageSize _size){
       return "http://image.tmdb.org/t/p/" + _size.toString() + pImagePath;
    }

    public String getReleaseDate(){
        return pReleaseDate;
    }

    public String getUserRating(){
        return pUserRating;
    }

    public String getSynopsis(){
        return pSynopsis;
    }

    public String getMovieTitle(){
        return pTitle;
    }
}
