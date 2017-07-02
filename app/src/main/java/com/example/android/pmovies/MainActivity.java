package com.example.android.pmovies;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.android.pmovies.tools.BrowseType;
import com.example.android.pmovies.tools.Movie;
import com.example.android.pmovies.tools.NetworkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Spinner viewSpinner;
    public static Movie[] movies;

    RecycleAdapter mAdapter;
    RecyclerView mRecyclerView;

    public static String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = getResources().getString(R.string.api_key);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        viewSpinner = (Spinner) findViewById(R.id.view_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.view_spinner_array, android.R.layout.simple_spinner_dropdown_item);
        viewSpinner.setAdapter(adapter);

        //When Ok button is clicked
        final Button button = (Button) findViewById(R.id.view_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               if(viewSpinner.getSelectedItem().toString() == getResources().getString(R.string.popular_movies)){
                   getPopularMovies();
               }
               else if(viewSpinner.getSelectedItem().toString() == getResources().getString(R.string.top_rated_movies)){
                   getTopRatedMovies();
               }
            }
        });
    }

    protected void getPopularMovies(){
        new queryTask().execute(NetworkTools.buildURL(BrowseType.POPULAR));
    }

    protected void getTopRatedMovies(){
        new queryTask().execute(NetworkTools.buildURL(BrowseType.TOP_RATED));
    }

    private class queryTask extends AsyncTask<URL, Void, String> {

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
                try{
                    //Get the json data
                    JSONObject jsonData = new JSONObject(rawJsonData);
                    JSONArray results = jsonData.getJSONArray("results");

                    //put each of them into movies array
                    movies = new Movie[results.length()];
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movie = results.getJSONObject(i);
                        movies[i] = new Movie(movie.getString("title"),movie.getString("poster_path"),movie.getString("overview"),movie.getString("vote_average"),movie.getString("release_date"));
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            mAdapter = new RecycleAdapter(movies);
            mRecyclerView.setAdapter(mAdapter);
        }

    }
}
