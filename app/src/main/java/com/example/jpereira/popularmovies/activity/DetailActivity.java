package com.example.jpereira.popularmovies.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.classes.Movie;
import com.example.jpereira.popularmovies.databinding.ActivityDetailBinding;
import com.example.jpereira.popularmovies.utilities.JsonParser;
import com.example.jpereira.popularmovies.utilities.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private ActivityDetailBinding activityDetailBinding;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(Movie.EXTRA_NAME)) {
                mMovie = intent.getParcelableExtra(Movie.EXTRA_NAME);

                activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

                Picasso.with(this).load(mMovie.getmImageUrl()).into(activityDetailBinding.ivPosterDetail);
                activityDetailBinding.tvMovieTitle.setText(mMovie.getmOriginalTitle());
                activityDetailBinding.tvReleaseDateValue.setText(mMovie.getmReleaseDate());
                float rating = (float)mMovie.getmRating();
                activityDetailBinding.rbRating.setRating((rating/2));
                activityDetailBinding.tvRatingValue.setText("(" + rating + "/10)");
                activityDetailBinding.tvOverviewValue.setText(mMovie.getmSynopsis());

                Log.i(TAG, mMovie.toString());

            }
        }

    }

//    public class MovieTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mainBinding.gvMoviesDisplay.setVisibility(View.INVISIBLE);
//            mainBinding.pbLoading.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String param = params[0];
//            String response = null;
//
//            URL url;
//            try {
//                url = NetworkUtil.buildUrl(param);
//                response = NetworkUtil.getResponseFromHttpUrl(url);
//            } catch (MalformedURLException e) {
//                Log.e(TAG, e.getMessage());
//            } catch (IOException e) {
//                Log.e(TAG, e.getMessage());
//            }
//
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            mListMovies = JsonParser.convertDataFromJsonString(s);
//
//            if(mListMovies != null) {
//                fetchData(mListMovies);
//                mainBinding.pbLoading.setVisibility(View.INVISIBLE);
//                mainBinding.gvMoviesDisplay.setVisibility(View.VISIBLE);
//            } else {
//                Toast.makeText(MainActivity.this,"Error to fetch data", Toast.LENGTH_LONG).show();
//                mainBinding.pbLoading.setVisibility(View.INVISIBLE);
//                mainBinding.btRetry.setVisibility(View.VISIBLE);
//            }
//        }
//    }
}
