package com.example.jpereira.popularmovies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.adapter.MovieAdapter;
import com.example.jpereira.popularmovies.classes.Movie;
import com.example.jpereira.popularmovies.utilities.JsonParser;
import com.example.jpereira.popularmovies.utilities.NetworkUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MovieAdapter mAdapter;
    private GridView mGridView;
    private ProgressBar mLoadingIndicator;
    private Button mButtonRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gv_movies_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading);
        mButtonRetry = (Button) findViewById(R.id.bt_retry);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie mMovie = (Movie) parent.getItemAtPosition(position);
                openDetail(mMovie);
            }
        });

        getMoviesData();
    }

    private void getMoviesData() {
        new MovieTask().execute("popular");
    }

    public void onGetData(View view) {
        mButtonRetry.setVisibility(View.INVISIBLE);
        getMoviesData();
    }

    private void openDetail(Movie m) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Movie.EXTRA_NAME, m);

        startActivity(intent);
    }

    public class MovieTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mGridView.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String sort = params[0];
            String response = null;

            URL url = NetworkUtil.buildUrl(sort);
            try {
                response = NetworkUtil.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            List<Movie> list = JsonParser.convertDataFromJsonString(s);

            if(list != null) {
                mAdapter = new MovieAdapter(MainActivity.this, list);
                mGridView.setAdapter(mAdapter);

                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mGridView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(MainActivity.this,"Error to fetch data, verify your internet connection", Toast.LENGTH_LONG).show();
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mButtonRetry.setVisibility(View.VISIBLE);
            }
        }
    }
}
