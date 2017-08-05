package com.example.android.pmovies.tools;

import android.app.Application;
import android.net.Uri;

import com.example.android.pmovies.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ref on 7/2/2017.
 */

public class NetworkTools extends Application{
    final static String PARAM_API = "api_key";
    final static String URL_POPULAR = "http://api.themoviedb.org/3/movie/popular";
    final static String URL_TOPRATED = "http://api.themoviedb.org/3/movie/top_rated";

    final static String URL_BASE = "http://api.themoviedb.org/3/movie/";

    public static URL buildURL(boolean searchPopular){
        URL output = null;

        String baseURL = null;
        if(searchPopular == true){
            baseURL = URL_POPULAR;
        }else{
            baseURL = URL_TOPRATED;
        }

        Uri buildUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(PARAM_API, GlobalVar.API_KEY)
                .build();

        try{
            output = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return output;
    }

    public static URL buildMovieReviewsURL(String movieID) throws MalformedURLException {
        URL output = null;

        if(movieID != null && movieID != ""){
            Uri buildUri = Uri.parse(URL_BASE + movieID +"/reviews").buildUpon()
                    .appendQueryParameter(PARAM_API, GlobalVar.API_KEY)
                    .build();

            try{
                output = new URL(buildUri.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            return output;
        }
        return output;
    }

    public static URL buildMovieTrailersURL(String movieID) throws MalformedURLException {
        URL output = null;

        if(movieID != null && movieID != ""){
            Uri buildUri = Uri.parse(URL_BASE + movieID +"/videos").buildUpon()
                    .appendQueryParameter(PARAM_API, GlobalVar.API_KEY)
                    .build();

            try{
                output = new URL(buildUri.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            return output;
        }
        return output;
    }

    public static String getHTTPResponse(URL url) throws IOException{
        HttpURLConnection   urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setConnectTimeout(5000);
        urlConn.setReadTimeout(10000);
        try{
            InputStream istream = urlConn.getInputStream();
            Scanner scanner = new Scanner(istream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput == true){
                return scanner.next();
            }else{
                return null;
            }
        }finally{
            urlConn.disconnect();
        }
    }
}

