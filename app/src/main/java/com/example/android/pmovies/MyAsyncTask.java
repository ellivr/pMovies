package com.example.android.pmovies;

import android.os.AsyncTask;

import com.example.android.pmovies.tools.Movie;
import com.example.android.pmovies.tools.NetworkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.android.pmovies.MainActivity.movies;

/**
 * Created by ref on 7/3/2017.
 */

public class MyAsyncTask extends AsyncTask<URL, Void, String> {
    public AsyncResponse delegate = null;//Call back interface

    public MyAsyncTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    @Override
    protected String doInBackground(URL... params) {
        URL searchUrl = params[0];
        String output = null;
        try {
            output = NetworkTools.getHTTPResponse(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    protected void onPostExecute(String rawJsonData) {
        if (rawJsonData != null && !rawJsonData.equals("")) {
            try {
                //Get the json data
                JSONObject jsonData = new JSONObject(rawJsonData);
                JSONArray results = jsonData.getJSONArray("results");

                //put each of them into movies array
                movies = new Movie[results.length()];
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movie = results.getJSONObject(i);
                    movies[i] = new Movie(movie.getString("title"), movie.getString("poster_path"), movie.getString("overview"), movie.getString("vote_average"), movie.getString("release_date"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            delegate.processFinish(movies);
        }

    }
}
