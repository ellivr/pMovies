package com.example.android.pmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pmovies.tools.ImageSize;
import com.example.android.pmovies.tools.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    Movie selectedMovie;

    TextView titleTextView;
    TextView releasedateTextView;
    TextView userratingTextView;
    TextView synopsisTextView;
    ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intentStarter = getIntent();
        if(intentStarter.hasExtra(Intent.EXTRA_TEXT)){
            selectedMovie = MainActivity.movies[Integer.parseInt(intentStarter.getStringExtra(Intent.EXTRA_TEXT))];

            titleTextView = (TextView)findViewById(R.id.detail_title);
            titleTextView.setText(selectedMovie.getMovieTitle());

            releasedateTextView = (TextView)findViewById(R.id.detail_releasedate);
            releasedateTextView.setText(String.format(getResources().getString(R.string.release_date), selectedMovie.getReleaseDate()));

            userratingTextView = (TextView)findViewById(R.id.detail_userrating);
            userratingTextView.setText(String.format(getResources().getString(R.string.rating), selectedMovie.getUserRating()));

            synopsisTextView = (TextView)findViewById(R.id.detail_synopsis);
            synopsisTextView.setText(selectedMovie.getSynopsis());

            posterImageView = (ImageView)findViewById(R.id.detail_image);
            Picasso.with(this).load(selectedMovie.getImagePath(ImageSize.w500)).into(posterImageView);
        }
    }
}
