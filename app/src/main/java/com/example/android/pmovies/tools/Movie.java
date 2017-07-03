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

    public String getImagePath(int _size){
        //Available size w92, w154, w185, w342, w500, w780
        switch (_size){
            case 92:
            case 154:
            case 185:
            case 342:
            case 500:
            case 780:
                return "http://image.tmdb.org/t/p/w" + _size + pImagePath;
            default:
                return "http://image.tmdb.org/t/p/original" + pImagePath;
        }
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
