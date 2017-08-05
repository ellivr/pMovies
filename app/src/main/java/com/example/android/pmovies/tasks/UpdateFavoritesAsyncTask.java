package com.example.android.pmovies.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.pmovies.R;
import com.example.android.pmovies.database.MovieContract;
import com.example.android.pmovies.database.MovieDbHelper;
import com.example.android.pmovies.tools.Movie;

/**
 * Created by ref on 8/5/2017.
 */

public class UpdateFavoritesAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private Movie movie;
    private Boolean isAlreadyFavorite;
    private Button favButton;


    public UpdateFavoritesAsyncTask(Context mContext, Movie movie, Boolean isAlreadyFavorite, Button favButton) {
        this.mContext = mContext;
        this.movie = movie;
        this.isAlreadyFavorite = isAlreadyFavorite;
        this.favButton = favButton;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (!isAlreadyFavorite) {
            ContentValues contentValues = MovieDbHelper.toContentValue(movie);
            mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        } else {
            mContext.getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry._ID + " = ?",
                    new String[]{movie.getId()}
            );
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        int toastStrRes;
        if (!isAlreadyFavorite) {
            toastStrRes = R.string.fav_msg_added;
            favButton.setText(mContext.getString(R.string.mark_unfavorite));
        } else {
            toastStrRes = R.string.fav_msg_removed;
            favButton.setText(mContext.getString(R.string.mark_favorite));
        }
        Toast.makeText(mContext, mContext.getString(toastStrRes), Toast.LENGTH_SHORT).show();
    }
}
