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
import com.example.jpereira.popularmovies.databinding.TrailerItemBinding;

/**
 * Created by jpereira on 02/03/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final Context mContext;

    final private TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClickTrailer(String url);
    }

    private Cursor mCursor;

    public TrailerAdapter(@NonNull Context context, TrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.trailer_item, viewGroup, false);

        view.setFocusable(true);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        trailerAdapterViewHolder.mTrailerBinding.tvVideoName.setText(mCursor.getString(mCursor.getColumnIndex("name")));

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

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TrailerItemBinding mTrailerBinding;

        TrailerAdapterViewHolder(View view) {
            super(view);

            mTrailerBinding = DataBindingUtil.bind(view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String url = mCursor.getString(mCursor.getColumnIndex("key"));
            mClickHandler.onClickTrailer(url);
        }
    }
}
