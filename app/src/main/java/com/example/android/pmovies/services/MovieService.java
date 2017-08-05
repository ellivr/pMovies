package com.example.android.pmovies.services;

import com.example.android.pmovies.tools.GlobalVar;
import com.example.android.pmovies.tools.ListResponse;
import com.example.android.pmovies.tools.Movie;
import com.example.android.pmovies.tools.Review;
import com.example.android.pmovies.tools.TrailersList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ref on 8/5/2017.
 */

public interface MovieService {

    public static final String PATH_MOVIE_POPULAR = "3/movie/popular?";
    public static final String PATH_MOVIE_TOP_RATED = "3/movie/top_rated?";

    @GET("3/movie/{sort}")
    Call<ListResponse<Movie>>  getMovies(@Path("sort") String sortOrder);

    @GET(GlobalVar.Const.PATH_MOVIE_REVIEWS)
    Call<ListResponse<Review>> getMovieReviews(@Path("id") String id);

    @GET(GlobalVar.Const.PATH_MOVIE_TRAILERS)
    Call<TrailersList> getMovieTrailers(@Path("id") String id);
}
