package com.example.android.pmovies;

import com.example.android.pmovies.tools.Movie;

/**
 * Created by ref on 7/3/2017.
 */

public interface AsyncResponse {
    void processFinish(Movie[] output);
}