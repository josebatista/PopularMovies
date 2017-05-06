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
import com.example.jpereira.popularmovies.adapters.ReviewAdapter;
import com.example.jpereira.popularmovies.databinding.ActivityDetailBinding;
import com.example.jpereira.popularmovies.utilities.JsonParser;
import com.example.jpereira.popularmovies.utilities.NetworkUtil;

import java.io.IOException;
import java.net.URL;

public class ReviewLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ReviewLoader.class.getSimpleName();

    private ReviewAdapter mReviewAdapter;

    private ActivityDetailBinding mDetailBinding;
    private Context mContext;

    public ReviewLoader(Context mContext, ActivityDetailBinding detailBinding, ReviewAdapter adapter) {
        this.mContext = mContext;
        this.mDetailBinding = detailBinding;
        this.mReviewAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this.mContext) {

            Cursor cursorReview;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) {
                    return;
                }

                mDetailBinding.rvReviews.setVisibility(View.INVISIBLE);
                mDetailBinding.pbLoadingTrailer.setVisibility(View.VISIBLE);

                if (cursorReview != null) {
                    deliverResult(cursorReview);
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
                        URL url = NetworkUtil.buildUrl(id + "/reviews");

                        String json = NetworkUtil.getResponseFromHttpUrl(url);
                        response = JsonParser.convertReviewToCursor(json);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                return response;
            }

            @Override
            public void deliverResult(Cursor data) {
                cursorReview = data;
                setDataToAdapter(cursorReview);
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            mReviewAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void setDataToAdapter(Cursor data) {
        if (data != null) {
            mReviewAdapter.swapCursor(data);

            mDetailBinding.pbLoadingTrailer.setVisibility(View.INVISIBLE);
            mDetailBinding.rvReviews.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this.mContext, "Error to fetch reviews data", Toast.LENGTH_LONG).show();
            mDetailBinding.pbLoadingTrailer.setVisibility(View.INVISIBLE);
        }
    }
}
