package com.example.android.pmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pmovies.adapters.ReviewAdapter;
import com.example.android.pmovies.adapters.TrailerAdapter;
import com.example.android.pmovies.services.MovieClient;
import com.example.android.pmovies.services.MovieService;
import com.example.android.pmovies.tasks.ManageFavoritesAsyncTask;
import com.example.android.pmovies.tools.GlobalVar;
import com.example.android.pmovies.tools.ListResponse;
import com.example.android.pmovies.tools.Movie;
import com.example.android.pmovies.tools.Review;
import com.example.android.pmovies.tools.Trailer;
import com.example.android.pmovies.tools.TrailersList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ref on 8/5/2017.
 */

public class DetailActivityFragment  extends Fragment implements TrailerAdapter.Callbacks {

    Movie selectedMovie;
    ReviewAdapter reviewAdapter;
    TrailerAdapter trailerAdapter;

    Context mContext;
    List<Review> mReviews;
    List<Trailer> mTrailers;

    final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    final String EXTRA_REVIEWS = "EXTRA_REVIEWS";
    final String EXTRA_SCROLL_POSITION = "EXTRA_SCROLL_POSITION";

    MovieService movieService;

    private ShareActionProvider mShareActionProvider;

    @BindView(R.id.movie_detail_scrollview) ScrollView scrollView;
    @BindView(R.id.movie_title) TextView titleTextView;
    @BindView(R.id.movie_desc) TextView synopsisTextView;
    @BindView(R.id.movie_rating) TextView userratingTextView;
    @BindView(R.id.movie_release_date) TextView releasedateTextView;
    @BindView(R.id.movie_poster) ImageView posterImageView;
    @BindView(R.id.favorite) Button favButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);
        Bundle args = getArguments();
        setHasOptionsMenu(true);

        if (args == null) {
            rootView.setVisibility(View.INVISIBLE);
            return rootView;
        }

        rootView.setVisibility(View.VISIBLE);
        selectedMovie = args.getParcelable(GlobalVar.MOVIE_TAG);
        if (selectedMovie != null) {
            titleTextView.setText(selectedMovie.getOriginalTitle());
            synopsisTextView.setText(selectedMovie.getOverview());
            releasedateTextView.setText(String.format(getResources().getString(R.string.release_date), selectedMovie.getRelease_date()));
            userratingTextView.setText(String.format(getResources().getString(R.string.rating), selectedMovie.getVoteAverage()));
            Picasso.with(getContext()).load(selectedMovie.getImageFullURL()).placeholder(R.mipmap.ic_autorenew_black_24dp).error(R.mipmap.ic_warning_black_24dp).into(posterImageView);
        }
        new ManageFavoritesAsyncTask(getActivity(), selectedMovie, false, favButton).execute();

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ManageFavoritesAsyncTask(getActivity(), selectedMovie,true, favButton).execute();
            }
        });


        reviewAdapter = new ReviewAdapter(new ArrayList<Review>());
        trailerAdapter = new TrailerAdapter(new ArrayList<Trailer>(), this);

        RecyclerView    reviewList = (RecyclerView) rootView.findViewById(R.id.review_list);
                        reviewList.setAdapter(reviewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView    trailerList = (RecyclerView) rootView.findViewById(R.id.trailer_list);
                        trailerList.setLayoutManager(layoutManager);
                        trailerList.setAdapter(trailerAdapter);
                        trailerList.setNestedScrollingEnabled(false);



        //Check if we already have Trailers in our saved instance
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
            List<Trailer> trailers = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
            trailerAdapter.add(trailers);
        } else {
            //If we don't have it, let's fetch them
            movieService = MovieClient.createService(MovieService.class);
            fetchTrailers();
        }

        //Check if we already have Reviews in our saved instance
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_REVIEWS)) {
            List<Review> reviews = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
            reviewAdapter.add(reviews);
        } else {
            //If we don't have it, let's fetch them
            movieService = MovieClient.createService(MovieService.class);
            fetchReviews();
        }

        //Check if we have scrollview position saved in our instance
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_SCROLL_POSITION)) {
            //Grab it
            final int[] position = savedInstanceState.getIntArray(EXTRA_SCROLL_POSITION);
            if(position != null)
                scrollView.post(new Runnable() {
                    public void run() {
                        //And get back to where we were
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });
        }

       // fetchReviews();
      //  fetchTrailers();
        mContext = getActivity();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflate menu resource file.
        inflater.inflate(R.menu.menu_detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item  = menu.findItem(R.id.share_trailer);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item );

        // Return true to display menu
        //return true;
    }

    //Saving the states
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try{
            mTrailers = trailerAdapter.getTrailers();
            if (mTrailers != null && !mTrailers.isEmpty()) {
                //Our trailers
                outState.putParcelableArrayList(EXTRA_TRAILERS, (ArrayList<? extends Parcelable>) mTrailers);
            }

            mReviews = reviewAdapter.getReviews();
            if (mReviews != null && !mReviews.isEmpty()) {
                //Our reviews
                outState.putParcelableArrayList(EXTRA_REVIEWS, (ArrayList<? extends Parcelable>) mReviews);
            }
        }catch (NullPointerException e){

        }

        //As well as the current scrollview position
        outState.putIntArray(EXTRA_SCROLL_POSITION, new int[]{ scrollView.getScrollX(), scrollView.getScrollY()});
    }

    // Call to update the share intent
    private void setShareIntent(Trailer trailer) {
        if (mShareActionProvider != null) {
            try{
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, selectedMovie.getOriginalTitle());

                String body = getResources().getString(R.string.share_body);
                body = body + selectedMovie.getOriginalTitle() + "\n" + trailer.getTrailerUrl();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                mShareActionProvider.setShareIntent(shareIntent);
            }catch (IllegalStateException ignored){

            }
        }
    }

    @Override
    public void watch(Trailer trailer, int position) {
        String videoURI = GlobalVar.Const.YOUTUBE_PREFIX + trailer.getKey();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURI));
        if(i.resolveActivity(mContext.getPackageManager()) != null){
            startActivity(i);
        }else{
            Toast.makeText(getActivity(), getResources().getString(R.string.error_intent), Toast.LENGTH_LONG).show();
        }
    }

    private void fetchReviews() {
        Call<ListResponse<Review>> reviewCall = movieService.getMovieReviews(selectedMovie.getId());
        reviewCall.enqueue(new Callback<ListResponse<Review>>() {
            @Override
            public void onResponse(Response<ListResponse<Review>> response) {
                try{
                    List<Review> reviews = response.body().getResults();
                    reviewAdapter.add(reviews);
                }catch (NullPointerException ignored){

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_fetchreviews), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTrailers() {
        Call<TrailersList> trailersListCall = movieService.getMovieTrailers(selectedMovie.getId());
        trailersListCall.enqueue(new Callback<TrailersList>() {
            @Override
            public void onResponse(Response<TrailersList> response) {
                try{
                    List<Trailer> trailers = response.body().getResults();
                    trailerAdapter.add(trailers);
                    if (trailers != null && !trailers.isEmpty()) {
                        setShareIntent(trailers.get(0));
                    }
                }catch (NullPointerException ignored){

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_fetchtrailer), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
