package com.example.android.pmovies.tools;

/**
 * Created by ref on 8/5/2017.
 */

public class GlobalVar {
    public final static String MOVIE_TAG = "MOVIE";
    public static String API_KEY = "";

    public final static class Const {
        public static final String BASE_URL = "http://api.themoviedb.org/";
        public static final String APP_KEY_QUERY_PARAM = "api_key";

        public static final String PATH_MOVIE_POPULAR = "3/movie/popular?";
        public static final String PATH_MOVIE_TOP_RATED = "3/movie/top_rated?";
        public static final String PATH_MOVIE_REVIEWS = "3/movie/{id}/reviews?";
        public static final String PATH_MOVIE_TRAILERS = "3/movie/{id}/videos?";


        public static final String SORT_POPULAR = "popular";
        public static final String SORT_RATING = "top_rated";
        public static final String SORT_FAV = "favourites";
        public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        public final static String IMAGE_SMALL_SIZE = "w185";
        public final static String IMAGE_DEFAULT_URL = "http://image.tmdb.org/t/p/w185/";

        public final static String YOUTUBE_PREFIX = "vnd.youtube:";
        public final static String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

        //ex: http://img.youtube.com/vi/c38r-SAnTWM/0.jpg
        public final static String YOUTUBE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";
        public final static String YOUTUBE_THUMBNAIL_SUFFIX = "/0.jpg";

    }
}
