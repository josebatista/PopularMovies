package com.example.jpereira.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.classes.Movie;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(Movie.EXTRA_NAME)) {
                Movie mMovie = intent.getParcelableExtra(Movie.EXTRA_NAME);

                Log.d(TAG, mMovie.toString());

                TextView mTextDebug = (TextView) findViewById(R.id.tv_debug);
                mTextDebug.setText(mMovie.toString());

            }
        }

    }
}
