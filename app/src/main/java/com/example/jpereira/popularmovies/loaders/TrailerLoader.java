package com.example.jpereira.popularmovies.loaders;

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

import com.example.jpereira.popularmovies.activity.DetailActivity;
import com.example.jpereira.popularmovies.adapters.TrailerAdapter;
import com.example.jpereira.popularmovies.databinding.ActivityDetailBinding;
import com.example.jpereira.popularmovies.utilities.JsonParser;
import com.example.jpereira.popularmovies.utilities.NetworkUtil;

import java.io.IOException;
import java.net.URL;

public class TrailerLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = TrailerLoader.class.getSimpleName();

    private TrailerAdapter mTrailerAdapter;

    private ActivityDetailBinding mDetailBinding;
    private Context mContext;

    public TrailerLoader(Context mContext, ActivityDetailBinding detailBinding, TrailerAdapter adapter) {
        this.mContext = mContext;
        this.mDetailBinding = detailBinding;
        this.mTrailerAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this.mContext) {

            Cursor cursorTrailers;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) {
                    return;
                }

                mDetailBinding.rvTrailer.setVisibility(View.INVISIBLE);
                mDetailBinding.pbLoadingTrailer.setVisibility(View.VISIBLE);

                if (cursorTrailers != null) {
                    deliverResult(cursorTrailers);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                String id = args.getString(DetailActivity.ID);
                Cursor response = null;

                if (NetworkUtil.isOnline(getContext())) {
                    if (id == null || TextUtils.isEmpty(id)) {
                        return null;
                    }
                    try {
                        URL url = NetworkUtil.buildUrl(id + "/videos");

                        String json = NetworkUtil.getResponseFromHttpUrl(url);
                        response = JsonParser.convertTrailerToCursor(json);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                return response;
            }

            @Override
            public void deliverResult(Cursor data) {
                cursorTrailers = data;
                setDataToAdapter(cursorTrailers);
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            mTrailerAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void setDataToAdapter(Cursor data) {
        if (data != null) {
            mTrailerAdapter.swapCursor(data);

            mDetailBinding.pbLoadingTrailer.setVisibility(View.INVISIBLE);
            mDetailBinding.rvTrailer.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this.mContext, "Error to fetch trailer data", Toast.LENGTH_LONG).show();
            mDetailBinding.pbLoadingTrailer.setVisibility(View.INVISIBLE);
        }
    }
}
