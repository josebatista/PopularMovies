package com.example.jpereira.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.databinding.ReviewItemBinding;
import com.example.jpereira.popularmovies.databinding.TrailerItemBinding;
import com.example.jpereira.popularmovies.utilities.JsonParser;

/**
 * Created by jpereira on 02/03/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.TrailerAdapterViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    public ReviewAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.review_item, viewGroup, false);

        view.setFocusable(true);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        trailerAdapterViewHolder.mReviewBinding.tvReviewAuthor.setText(mCursor.getString(mCursor.getColumnIndex(JsonParser.REVIEW_AUTHOR)));
        trailerAdapterViewHolder.mReviewBinding.tvReviewContent.setText(mCursor.getString(mCursor.getColumnIndex(JsonParser.REVIEW_CONTENT)));

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {

        final ReviewItemBinding mReviewBinding;

        TrailerAdapterViewHolder(View view) {
            super(view);

            mReviewBinding = DataBindingUtil.bind(view);
        }
    }
}
