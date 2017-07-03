package com.example.android.pmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pmovies.tools.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    Movie selectedMovie;

    @BindView(R.id.detail_title) TextView titleTextView;
    @BindView(R.id.detail_releasedate) TextView releasedateTextView;
    @BindView(R.id.detail_userrating) TextView userratingTextView;
    @BindView(R.id.detail_synopsis) TextView synopsisTextView;
    @BindView(R.id.detail_image) ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intentStarter = getIntent();
        if(intentStarter.hasExtra(Intent.EXTRA_TEXT)){
            selectedMovie = MainActivity.movies[Integer.parseInt(intentStarter.getStringExtra(Intent.EXTRA_TEXT))];

            titleTextView.setText(selectedMovie.getMovieTitle());
            releasedateTextView.setText(String.format(getResources().getString(R.string.release_date), selectedMovie.getReleaseDate()));
            userratingTextView.setText(String.format(getResources().getString(R.string.rating), selectedMovie.getUserRating()));
            synopsisTextView.setText(selectedMovie.getSynopsis());

            Picasso.with(this).load(selectedMovie.getImagePath(500)).into(posterImageView);
        }
    }
}
