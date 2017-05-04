package com.example.jpereira.popularmovies.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.classes.Movie;
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
}
