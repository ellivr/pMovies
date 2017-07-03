package com.example.android.pmovies;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.pmovies.tools.Movie;
import com.example.android.pmovies.tools.NetworkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    public static Movie[] movies;

    RecycleAdapter mAdapter;

    @BindView(R.id.view_spinner) Spinner viewSpinner;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    public static String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        API_KEY = getResources().getString(R.string.api_key);

        mAdapter = new RecycleAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.view_spinner_array, android.R.layout.simple_spinner_dropdown_item);
            viewSpinner.setAdapter(adapter);
            viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       if(viewSpinner.getSelectedItem().toString() == getResources().getString(R.string.popular_movies)){
                           getPopularMovies();
                       }
                       else if(viewSpinner.getSelectedItem().toString() == getResources().getString(R.string.top_rated_movies)){
                           getTopRatedMovies();
                       }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
    }

    //When users selected Popular Movies on viewSpinner
    protected void getPopularMovies(){
        MyAsyncTask asyncTask = new MyAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(Movie[] output) {
            mAdapter.swapData(movies);
            mRecyclerView.setAdapter(mAdapter);
            }
        });
        //Start the asyncTask
        asyncTask.execute(NetworkTools.buildURL(true));
    }

    //When users selected Top Rated Movies on viewSpinner
    protected void getTopRatedMovies(){
        MyAsyncTask asyncTask = new MyAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(Movie[] output) {
                mAdapter.swapData(movies);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        //Start the asyncTask
        asyncTask.execute(NetworkTools.buildURL(false));
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

}
