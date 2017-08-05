package com.example.android.pmovies.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.example.android.pmovies.R;
import com.example.android.pmovies.database.MovieDbHelper;
import com.example.android.pmovies.tools.Movie;

/**
 * Created by ref on 8/5/2017.
 */

public class ManageFavoritesAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private Movie movie;
    private Boolean performAction;
    private Button favButton;

    public ManageFavoritesAsyncTask(Context mContext, Movie movie, Boolean performAction, Button favButton) {
        this.mContext = mContext;
        this.movie = movie;
        this.performAction = performAction;
        this.favButton = favButton;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return MovieDbHelper.isFavourite(mContext, movie.getId());
    }


    @Override
    protected void onPostExecute(Boolean isFavorited) {
        if (performAction) {
            new UpdateFavoritesAsyncTask(mContext, movie, isFavorited, favButton).execute();
        } else {
            if (isFavorited) {
                favButton.setText(mContext.getString(R.string.mark_unfavorite));
            } else {
                favButton.setText(mContext.getString(R.string.mark_favorite));
            }
        }
    }
}