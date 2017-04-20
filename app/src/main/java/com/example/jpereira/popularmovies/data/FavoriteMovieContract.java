package com.example.jpereira.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by jpereira on 19/04/17.
 */

public class FavoriteMovieContract {

    public FavoriteMovieContract() {
    }

    public static class FavoriteMovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorite_movie";

        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_POSTER = "poster";
        public static final String MOVIE_SYNOPSIS = "synopsis";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_RELEASE_DATE = "release_date";

    }
}
