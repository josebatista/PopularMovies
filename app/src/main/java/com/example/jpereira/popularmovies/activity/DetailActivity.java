package com.example.jpereira.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.adapters.ReviewAdapter;
import com.example.jpereira.popularmovies.adapters.TrailerAdapter;
import com.example.jpereira.popularmovies.classes.Movie;
import com.example.jpereira.popularmovies.data.FavoriteMovieContract;
import com.example.jpereira.popularmovies.databinding.ActivityDetailBinding;
import com.example.jpereira.popularmovies.loaders.ReviewLoader;
import com.example.jpereira.popularmovies.loaders.TrailerLoader;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final int TRAILER_LOADER = 43;
    private static final int REVIEW_LOADER = 44;

    public static final String ID = "id";

    private ActivityDetailBinding activityDetailBinding;
    private Movie mMovie;
    private TrailerLoader mTrailerLoader;
    private ReviewLoader mReviewLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.EXTRA_NAME)) {
                mMovie = intent.getParcelableExtra(MainActivity.EXTRA_NAME);

                activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

                Picasso.with(this)
                        .load(mMovie.getmImageUrl())
                        .error(android.R.drawable.ic_menu_report_image)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .into(activityDetailBinding.ivPosterDetail);

                activityDetailBinding.tvMovieTitle.setText(mMovie.getmOriginalTitle());
                activityDetailBinding.tvReleaseDateValue.setText(mMovie.getmReleaseDate());
                float rating = (float) mMovie.getmRating();
                activityDetailBinding.rbRating.setRating((rating / 2));
                activityDetailBinding.tvRatingValue.setText("(" + rating + "/10)");
                activityDetailBinding.tvOverviewValue.setText(mMovie.getmSynopsis());

                Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(mMovie.getmIdMovie()).build();

                Cursor c = getContentResolver().query(uri, null, null, null, FavoriteMovieContract.FavoriteMovieEntry._ID);

                if (c.getCount() > 0) {
                    //remove from fav
                    activityDetailBinding.btnFav.setText(R.string.remove_fav);
                } else {
                    //add to fav
                    activityDetailBinding.btnFav.setText(R.string.add_fav);
                }
                Log.i(TAG, mMovie.toString());

                TrailerAdapter mTrailerAdapter = new TrailerAdapter(this, this);

                LinearLayoutManager layoutManagerTrailer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityDetailBinding.rvTrailer.setLayoutManager(layoutManagerTrailer);
                activityDetailBinding.rvTrailer.setHasFixedSize(true);
                activityDetailBinding.rvTrailer.setAdapter(mTrailerAdapter);

                mTrailerLoader = new TrailerLoader(this, activityDetailBinding, mTrailerAdapter);

                Bundle bundle = new Bundle();
                bundle.putString(ID, mMovie.getmIdMovie());

                LoaderManager loaderManager = getSupportLoaderManager();
                Loader<Cursor> loaderTrailer = loaderManager.getLoader(TRAILER_LOADER);

                if (loaderTrailer == null) {
                    loaderManager.initLoader(TRAILER_LOADER, bundle, mTrailerLoader);
                } else {
                    loaderManager.restartLoader(TRAILER_LOADER, bundle, mTrailerLoader);
                }

                ReviewAdapter mReviewAdapter = new ReviewAdapter(this);

                LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityDetailBinding.rvReviews.setLayoutManager(layoutManagerReview);
                activityDetailBinding.rvReviews.setHasFixedSize(true);
                activityDetailBinding.rvReviews.setAdapter(mReviewAdapter);

                mReviewLoader = new ReviewLoader(this, activityDetailBinding, mReviewAdapter);

                Loader<Cursor> loaderReview = loaderManager.getLoader(REVIEW_LOADER);

                if (loaderReview == null) {
                    loaderManager.initLoader(REVIEW_LOADER, bundle, mReviewLoader);
                } else {
                    loaderManager.restartLoader(REVIEW_LOADER, bundle, mReviewLoader);
                }
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

        int i = getContentResolver().delete(uri, null, null);

        if (i == 0) {
            uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, cv);

            if (uri != null) {
                Toast.makeText(getBaseContext(), getString(R.string.added_success), Toast.LENGTH_LONG).show();
                activityDetailBinding.btnFav.setText(R.string.remove_fav);
            }
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.removed_success), Toast.LENGTH_LONG).show();
            activityDetailBinding.btnFav.setText(R.string.add_fav);
        }
    }

    @Override
    public void onClickTrailer(String url) {
        Uri uri = Uri.parse(url).buildUpon().build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
