package com.example.jpereira.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.classes.Movie;
import com.example.jpereira.popularmovies.data.FavoriteMovieContract;
import com.example.jpereira.popularmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private ActivityDetailBinding activityDetailBinding;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.EXTRA_NAME)) {
                mMovie = intent.getParcelableExtra(MainActivity.EXTRA_NAME);

                activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

                Picasso.with(this).load(mMovie.getmImageUrl()).into(activityDetailBinding.ivPosterDetail);
                activityDetailBinding.tvMovieTitle.setText(mMovie.getmOriginalTitle());
                activityDetailBinding.tvReleaseDateValue.setText(mMovie.getmReleaseDate());
                float rating = (float) mMovie.getmRating();
                activityDetailBinding.rbRating.setRating((rating / 2));
                activityDetailBinding.tvRatingValue.setText("(" + rating + "/10)");
                activityDetailBinding.tvOverviewValue.setText(mMovie.getmSynopsis());

                Log.i(TAG, mMovie.toString());
            }
        }
    }

    public void favoriteMovie(View v) {
        ContentValues cv = new ContentValues();

        cv.put(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_ID, mMovie.getmIdMovie());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_TITLE, mMovie.getmOriginalTitle());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_POSTER, mMovie.getmImageUrl());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_SYNOPSIS, mMovie.getmSynopsis());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_RATING, mMovie.getmRating());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_RELEASE_DATE, mMovie.getmReleaseDate());

        Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(mMovie.getmIdMovie()).build();

        Cursor c = getContentResolver().query(uri, null, null, null, FavoriteMovieContract.FavoriteMovieEntry._ID);

        if(c != null) {

            uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, cv);

            if (uri != null) {
                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
