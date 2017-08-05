package com.example.android.pmovies;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.pmovies.adapters.MovieAdapter;
import com.example.android.pmovies.services.MovieClient;
import com.example.android.pmovies.services.MovieService;
import com.example.android.pmovies.tasks.FetchFavoritesAsyncTask;
import com.example.android.pmovies.tools.GlobalVar;
import com.example.android.pmovies.tools.ListResponse;
import com.example.android.pmovies.tools.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ref on 8/5/2017.
 */

public class MainActivityFragment extends Fragment{

    public MovieAdapter mMovieAdapter;
    private MovieService movieService;
    private List<Movie> mMovies;

    private String mSortCriteria = GlobalVar.Const.SORT_POPULAR;
    private final String EXTRA_MOVIES = "EXTRA_MOVIES";
    private final String EXTRA_SORT_BY = "EXTRA_SORT_BY";

    private MenuItem mMenuItemSortPopular;
    private MenuItem mMenuItemSortRating;
    private MenuItem mMenuItemSortFav;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    public interface BunldeCallback {
        void onItemSelected(Movie movie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_SORT_BY, mSortCriteria);
        ArrayList<Movie> movies = (ArrayList<Movie>) mMovies;
        if (movies != null && !movies.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_MOVIES, movies);
        }
        // Needed to avoid confusion, when we back from detail screen (i. e. top rated selected but
        // favorite mMovies are shown and onCreate was not called in this case).
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mainfragment, menu);

        mMenuItemSortPopular = menu.findItem(R.id.action_sort_popular);
        mMenuItemSortRating = menu.findItem(R.id.action_sort_rating);
        mMenuItemSortFav = menu.findItem(R.id.action_sort_favourites);

        if (mSortCriteria.contentEquals(GlobalVar.Const.SORT_POPULAR)) {
            if (!mMenuItemSortPopular.isChecked()) {
                mMenuItemSortPopular.setChecked(true);
            }
        } else if (mSortCriteria.contentEquals(GlobalVar.Const.SORT_RATING)) {
            if (!mMenuItemSortRating.isChecked()) {
                mMenuItemSortRating.setChecked(true);
            }
        } else if (mSortCriteria.contentEquals(GlobalVar.Const.SORT_FAV)) {
            if (!mMenuItemSortFav.isChecked()) {
                mMenuItemSortFav.setChecked(true);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                if (isOnline()) {
                    mSortCriteria = GlobalVar.Const.SORT_POPULAR;
                    fetchMovies(mSortCriteria);
                    if (!mMenuItemSortPopular.isChecked()) {
                        mMenuItemSortPopular.setChecked(true);
                    }
                }
                return true;
            case R.id.action_sort_rating:
                if (isOnline()) {
                    mSortCriteria = GlobalVar.Const.SORT_RATING;
                    fetchMovies(mSortCriteria);
                    if (!mMenuItemSortRating.isChecked()) {
                        mMenuItemSortRating.setChecked(true);
                    }
                }
                return true;
            case R.id.action_sort_favourites:
                mSortCriteria = GlobalVar.Const.SORT_FAV;
                if (!mMenuItemSortFav.isChecked()) {
                    mMenuItemSortFav.setChecked(true);
                }
                new FetchFavoritesAsyncTask(getContext(), mMovieAdapter, mMovies).execute();
                return true;
            default:
                return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMovies = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(getActivity(), mMovies);

        GridView movieGrid = (GridView) rootView.findViewById(R.id.movie_grid);
        movieGrid.setAdapter(mMovieAdapter);
        movieService = MovieClient.createService(MovieService.class);

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BunldeCallback) getActivity()).onItemSelected(mMovies.get(position));
            }
        });

        if (isOnline()) {
            if (savedInstanceState != null) {
                mSortCriteria = savedInstanceState.getString(EXTRA_SORT_BY);
                mMovies = savedInstanceState.getParcelableArrayList(EXTRA_MOVIES);
                mMovieAdapter.clear();
                mMovieAdapter = new MovieAdapter(getActivity(), mMovies);

                movieGrid.setAdapter(mMovieAdapter);
                movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((BunldeCallback) getActivity()).onItemSelected(mMovies.get(position));
                    }
                });
            }else{
                fetchMovies(GlobalVar.Const.SORT_POPULAR);
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mSortCriteria == GlobalVar.Const.SORT_FAV){
            try{
                if (!mMenuItemSortFav.isChecked()) {
                    mMenuItemSortFav.setChecked(true);
                }
                new FetchFavoritesAsyncTask(getContext(), mMovieAdapter, mMovies).execute();
            }catch (NullPointerException ignored){

            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(getContext(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void fetchMovies(String sort_order) {
        Call<ListResponse<Movie>> moviesCall;
        moviesCall = movieService.getMovies(sort_order);

        moviesCall.enqueue(new Callback<ListResponse<Movie>>() {
            @Override
            public void onResponse(Response<ListResponse<Movie>> response) {
                try{
                    List<Movie> movieList = response.body().getResults();
                    mMovieAdapter.clear();
                    for (Movie movie : movieList) {
                        mMovieAdapter.add(movie);
                    }
                }catch(NullPointerException ignored){

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
