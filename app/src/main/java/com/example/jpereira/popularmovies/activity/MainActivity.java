package com.example.jpereira.popularmovies.activity;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.classes.Movie;
import com.example.jpereira.popularmovies.data.FavoriteMovieContract;
import com.example.jpereira.popularmovies.loaders.PopularMovieLoader;
import com.example.jpereira.popularmovies.databinding.ActivityMainBinding;
import com.example.jpereira.popularmovies.utilities.NetworkUtil;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String GET_POPULAR_MOVIES = "popular";
    public static final String GET_TOP_RATED_MOVIES = "top_rated";
    public static final String GET_FAVORITE_MOVIES = "favorite";
    public static final String EXTRA_NAME = "MOVIE";

    private static final String GV_POSITION = "position";

    private static final int POPULAR_MOVIE_LOADER = 42;


    private static String SORT = GET_POPULAR_MOVIES;

    private ActivityMainBinding mMainBinding;
    private PopularMovieLoader mPopularMovieLoader;
    private int mGridPosition = 0;

//    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mPopularMovieLoader = new PopularMovieLoader(this, mMainBinding);

//        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(this);
//        mDb = dbHelper.getReadableDatabase();

        mMainBinding.gvMoviesDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mGridPosition = position; //set position to save bundle
                mPopularMovieLoader.setPosition(mGridPosition); //set position to scroll

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                Movie mMovie = null;
                if (cursor.moveToPosition(position)) {
                    mMovie = new Movie(
                            cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_ID)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_TITLE)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_POSTER)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_SYNOPSIS)),
                            cursor.getDouble(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_RATING)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_RELEASE_DATE))
                    );

                }
//                cursor.close();
                openDetail(mMovie);
            }
        });

        getMoviesData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(GV_POSITION, mMainBinding.gvMoviesDisplay.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mGridPosition = savedInstanceState.getInt(GV_POSITION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPopularMovieLoader.setPosition(mGridPosition);
        mMainBinding.gvMoviesDisplay.setSelection(mGridPosition);
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
            case R.id.menu_favorite:
                SORT = GET_FAVORITE_MOVIES;
        }

        getMoviesData();

        return super.onOptionsItemSelected(item);
    }

    private void getMoviesData() {
        try {
            URL url = NetworkUtil.buildUrl(SORT);

            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_NAME, url.toString());
            bundle.putString(GET_POPULAR_MOVIES, SORT);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Cursor> loader = loaderManager.getLoader(POPULAR_MOVIE_LOADER);

            if (loader == null) {
                loaderManager.initLoader(POPULAR_MOVIE_LOADER, bundle, mPopularMovieLoader);
            } else {
                loaderManager.restartLoader(POPULAR_MOVIE_LOADER, bundle, mPopularMovieLoader);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void onGetData(View view) {
        mMainBinding.btRetry.setVisibility(View.INVISIBLE);
        getMoviesData();
    }

    private void openDetail(Movie m) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_NAME, m);

        startActivity(intent);
    }

}
