package com.example.jpereira.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.classes.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView mOriginalTitle;
    private ImageView mPoster;
    private TextView mSynopsis;
    private TextView mTextViewUserRating;
    private RatingBar mRatingBarUserRating;
    private TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(Movie.EXTRA_NAME)) {
                Movie mMovie = intent.getParcelableExtra(Movie.EXTRA_NAME);

                mOriginalTitle = (TextView) findViewById(R.id.tv_movie_title);
                mPoster = (ImageView) findViewById(R.id.iv_poster_detail);
                mSynopsis = (TextView) findViewById(R.id.tv_overview_value);
                mTextViewUserRating = (TextView) findViewById(R.id.tv_rating_value);
                mRatingBarUserRating = (RatingBar) findViewById(R.id.rb_rating);
                mReleaseDate = (TextView) findViewById(R.id.tv_release_date_value);

                Picasso.with(this).load(mMovie.getmImageUrl()).into(mPoster);
                mOriginalTitle.setText(mMovie.getmOriginalTitle());
                mReleaseDate.setText(mMovie.getmReleaseDate());
                float rating = (float)mMovie.getmRating();
                mRatingBarUserRating.setRating((rating/2));
                mTextViewUserRating.setText("(" + rating + "/10)");
                mSynopsis.setText(mMovie.getmSynopsis());

                Log.i(TAG, mMovie.toString());

            }
        }

    }
}
