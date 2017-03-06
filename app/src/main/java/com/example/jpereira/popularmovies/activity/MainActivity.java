package com.example.jpereira.popularmovies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LIST_MOVIES = "list_of_movies";
    private static final String TYPE_OF_SORT = "type_of_sort";

    private static final String GET_POPULAR_MOVIES = "popular";
    private static final String GET_TOP_RATED_MOVIES = "top_rated";

    private static String SORT = GET_POPULAR_MOVIES;

    private MovieAdapter mAdapter;
    private GridView mGridView;
    private ProgressBar mLoadingIndicator;
    private Button mButtonRetry;
    private ArrayList<Movie> mListMovies;

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

        if(savedInstanceState == null) {
            getMoviesData();
        } else {
            Log.i(TAG, "Loading saved instance");
            SORT = savedInstanceState.getString(TYPE_OF_SORT);
            mListMovies = savedInstanceState.getParcelableArrayList(LIST_MOVIES);
            fetchData(mListMovies);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, "Saving instance state");
        outState.putString(TYPE_OF_SORT, SORT);
        outState.putParcelableArrayList(LIST_MOVIES, mListMovies);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedId = item.getItemId();

        switch (itemClickedId) {
            case R.id.menu_popular:
                SORT = GET_POPULAR_MOVIES;
                break;
            case R.id.menu_top_rated:
                SORT = GET_TOP_RATED_MOVIES;
                break;
        }

        getMoviesData();

        return super.onOptionsItemSelected(item);
    }

    private void getMoviesData() {
        new MovieTask().execute(SORT);
    }

    public void onGetData(View view) {
        mButtonRetry.setVisibility(View.INVISIBLE);
        getMoviesData();
    }

    private void fetchData(ArrayList<Movie> mListMovies) {
        mAdapter = new MovieAdapter(MainActivity.this, mListMovies);
        mGridView.setAdapter(mAdapter);
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

            URL url;
            try {
                url = NetworkUtil.buildUrl(sort);
                response = NetworkUtil.getResponseFromHttpUrl(url);
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mListMovies = JsonParser.convertDataFromJsonString(s);

            if(mListMovies != null) {
                fetchData(mListMovies);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mGridView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(MainActivity.this,"Error to fetch data", Toast.LENGTH_LONG).show();
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mButtonRetry.setVisibility(View.VISIBLE);
            }
        }
    }
}
