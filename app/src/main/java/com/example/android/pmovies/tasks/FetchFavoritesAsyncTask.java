package com.example.android.pmovies.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.pmovies.adapters.MovieAdapter;
import com.example.android.pmovies.database.MovieContract;
import com.example.android.pmovies.tools.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ref on 8/5/2017.
 */

public class FetchFavoritesAsyncTask  extends AsyncTask<Void, Void, List<Movie>> {

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOV_TITLE,
            MovieContract.MovieEntry.COLUMN_MOV_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_MOV_IMAGE_PATH,
            MovieContract.MovieEntry.COLUMN_MOV_USER_RATING,
            MovieContract.MovieEntry.COLUMN_MOV_RELEASE_DATE
    };

    private MovieAdapter mMovieAdapter;

    public static final int COL_MOV_ID = 0;
    public static final int COL_MOV_TITLE = 1;
    public static final int COL_MOV_SYNOPSIS = 2;
    public static final int COL_MOV_IMAGE_PATH = 3;
    public static final int COL_MOV_USER_RATING = 4;
    public static final int COL_MOV_RELEASE_DATE = 5;

    private Context mContext;
    private List<Movie> mMovies;

    public FetchFavoritesAsyncTask(Context context, MovieAdapter movieAdapter, List<Movie> movies) {
        mContext = context;
        mMovieAdapter = movieAdapter;
        mMovies = movies;
    }

    private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
        List<Movie> results = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getString(COL_MOV_ID),
                        cursor.getString(COL_MOV_TITLE),
                        cursor.getString(COL_MOV_SYNOPSIS),
                        cursor.getString(COL_MOV_IMAGE_PATH),
                        cursor.getString(COL_MOV_USER_RATING),
                        cursor.getString(COL_MOV_RELEASE_DATE));
                results.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
        return getFavoriteMoviesDataFromCursor(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            if (mMovieAdapter != null) {
                mMovieAdapter.clear();
                mMovies = new ArrayList<>();
                mMovies.addAll(movies);
                for (Movie movie : movies) {
                    mMovieAdapter.add(movie);
                }
            }
        }
    }
}
