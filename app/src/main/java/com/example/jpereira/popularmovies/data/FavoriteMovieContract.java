package com.example.jpereira.popularmovies.data;

import android.content.res.Resources;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.jpereira.popularmovies.R;

/**
 * Created by jpereira on 19/04/17.
 */

public class FavoriteMovieContract {

    public FavoriteMovieContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.jpereira.popularmovies";
    public static final String PATH_MOVIE = "movie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "favorite_movie";

        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_POSTER = "poster";
        public static final String MOVIE_SYNOPSIS = "synopsis";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_RELEASE_DATE = "release_date";

    }
}
