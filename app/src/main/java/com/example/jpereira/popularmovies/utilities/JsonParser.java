package com.example.jpereira.popularmovies.utilities;

import com.example.jpereira.popularmovies.classes.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpereira on 03/03/17.
 */

public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();

    private static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/w185/";


    private static final String RESULTS = "results";

    private static final String ORIGINAL_TITLE = "original_title";
    private static final String MOVIE_POSTER_IMAGE = "poster_path";
    private static final String SYNOPSIS = "overview";
    private static final String USER_RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";


    public static List<Movie> convertDataFromJsonString(String json) {
        List<Movie> list = null;

        if(json != null) {
            list = new ArrayList<>();
            try {
                JSONObject jsonObjectMovies = new JSONObject(json);
                JSONArray mArrayMovies = jsonObjectMovies.getJSONArray(RESULTS);

                for(int i = 0; i < mArrayMovies.length(); i++) {
                    JSONObject mMovie = mArrayMovies.getJSONObject(i);

                    String mOriginalTitle = mMovie.getString(ORIGINAL_TITLE);
                    String mPoster = BASE_URL_POSTER + mMovie.getString(MOVIE_POSTER_IMAGE);
                    String mSynopsis = mMovie.getString(SYNOPSIS);
                    double mUserRating = mMovie.getDouble(USER_RATING);
                    String mReleaseDate = mMovie.getString(RELEASE_DATE);

                    list.add(new Movie(mOriginalTitle, mPoster, mSynopsis, mUserRating, mReleaseDate));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
