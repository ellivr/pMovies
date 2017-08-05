package com.example.android.pmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pmovies.database.MovieContract.MovieEntry;
import com.example.android.pmovies.tools.Movie;

/**
 * Created by ref on 8/4/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "pMovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_MOVIE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "(" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_MOV_TITLE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_MOV_SYNOPSIS + " TEXT NOT NULL," +
                MovieEntry.COLUMN_MOV_IMAGE_PATH + " TEXT NOT NULL," +
                MovieEntry.COLUMN_MOV_RELEASE_DATE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_MOV_USER_RATING + " TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);

    }

    public static ContentValues toContentValue(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry._ID, movie.getId());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOV_TITLE, movie.getOriginalTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOV_SYNOPSIS, movie.getOverview());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOV_IMAGE_PATH, movie.getPosterPath());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOV_RELEASE_DATE, movie.getRelease_date());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOV_USER_RATING, movie.getVoteAverage());
        return movieValues;
    }

    public static boolean isFavourite(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,   // projection
                MovieContract.MovieEntry._ID + " = ?", // selection
                new String[]{id},   // selectionArgs
                null    // sort order
        );
        if (cursor != null) {
            int numRows = cursor.getCount();
            cursor.close();
            return (numRows > 0);
        }
        return false;
    }

}

