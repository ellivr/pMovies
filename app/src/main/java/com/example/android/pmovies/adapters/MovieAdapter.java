package com.example.android.pmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pmovies.R;
import com.example.android.pmovies.tools.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ref on 8/5/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private final List<Movie>  mMovies;

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        mMovies = movies;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recyclerview_item, parent, false);
        }

        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_image);
        if (movie != null) {
            Picasso.with(getContext()).load(movie.getImageFullURL()).placeholder(R.mipmap.ic_autorenew_black_24dp).error(R.mipmap.ic_warning_black_24dp).into(moviePoster);
        }

        return convertView;
    }

    public List<Movie>  getMovies() {
        return mMovies;
    }
}
