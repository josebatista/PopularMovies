package com.example.jpereira.popularmovies.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jpereira.popularmovies.activity.MainActivity;
import com.example.jpereira.popularmovies.adapter.MovieAdapterCursor;
import com.example.jpereira.popularmovies.data.FavoriteMovieContract;
import com.example.jpereira.popularmovies.databinding.ActivityMainBinding;
import com.example.jpereira.popularmovies.utilities.JsonParser;
import com.example.jpereira.popularmovies.utilities.NetworkUtil;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jose on 04/05/17.
 */

public class PopularMovieLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = PopularMovieLoader.class.getSimpleName();

    private MovieAdapterCursor mAdapterCursor;

    private ActivityMainBinding mMainBinding;
    private Context mContext;
    private int mPosition;

    public PopularMovieLoader(Context mContext, ActivityMainBinding mainBinding) {
        this.mContext = mContext;
        this.mMainBinding = mainBinding;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this.mContext) {

            Cursor cursorMovies;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) {
                    return;
                }

                mMainBinding.gvMoviesDisplay.setVisibility(View.INVISIBLE);
                mMainBinding.pbLoading.setVisibility(View.VISIBLE);

                String sort = args.getString(MainActivity.GET_POPULAR_MOVIES);
                if (sort == MainActivity.GET_FAVORITE_MOVIES) {
                    forceLoad();
                } else {
                    if (cursorMovies != null) {
                        deliverResult(cursorMovies);
                    } else {
                        forceLoad();
                    }
                }
            }

            @Override
            public Cursor loadInBackground() {
                String urlArg = args.getString(MainActivity.EXTRA_NAME);
                String sort = args.getString(MainActivity.GET_POPULAR_MOVIES);
                Cursor response = null;

                switch (sort) {
                    case MainActivity.GET_POPULAR_MOVIES:
                    case MainActivity.GET_TOP_RATED_MOVIES:
                        if (NetworkUtil.isOnline(getContext())) {
                            if (urlArg == null || TextUtils.isEmpty(urlArg)) {
                                return null;
                            }

                            try {
                                URL url = new URL(urlArg);
                                String json = NetworkUtil.getResponseFromHttpUrl(url);
                                response = JsonParser.convertDataToCursor(json);
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                        break;
                    case MainActivity.GET_FAVORITE_MOVIES:
                        Log.d(TAG, "Load from DataBase!");

                        response = getContext().getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, null, null, null, FavoriteMovieContract.FavoriteMovieEntry._ID);

                        break;
                }
                return response;
            }

            @Override
            public void deliverResult(Cursor data) {
                cursorMovies = data;
                setDataToAdapter(cursorMovies);
                mMainBinding.gvMoviesDisplay.setSelection(mPosition);
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapterCursor.swapCursor(data);
        mMainBinding.gvMoviesDisplay.setSelection(this.mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterCursor.swapCursor(null);
    }

    private void setDataToAdapter(Cursor data) {
        if (data != null) {
            fetchDataCursor(data);
            mMainBinding.pbLoading.setVisibility(View.INVISIBLE);
            mMainBinding.gvMoviesDisplay.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this.mContext, "Error to fetch data", Toast.LENGTH_LONG).show();
            mMainBinding.pbLoading.setVisibility(View.INVISIBLE);
            mMainBinding.btRetry.setVisibility(View.VISIBLE);
        }
    }

    private void fetchDataCursor(Cursor mListMovies) {
        mAdapterCursor = new MovieAdapterCursor(this.mContext, mListMovies);
        mMainBinding.gvMoviesDisplay.setAdapter(mAdapterCursor);
    }
}
