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

    public static URL buildURL(BrowseType _type){
        URL output = null;

        String baseURL = null;
        if(_type == BrowseType.POPULAR){
            baseURL = URL_POPULAR;
        }else{
            baseURL = URL_TOPRATED;
        }

        Uri buildUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(PARAM_API, MainActivity.API_KEY)
                .build();

        try{
            output = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
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

