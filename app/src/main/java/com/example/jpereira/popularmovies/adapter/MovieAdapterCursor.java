package com.example.jpereira.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.data.FavoriteMovieContract;
import com.example.jpereira.popularmovies.databinding.ActivityGridViewItemBinding;
import com.squareup.picasso.Picasso;

/**
 * Created by jpereira on 02/03/17.
 */

public class MovieAdapterCursor extends CursorAdapter {

    private ActivityGridViewItemBinding mItemBinding;

    public MovieAdapterCursor(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.activity_grid_view_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mItemBinding = DataBindingUtil.bind(view);

        Picasso
                .with(context)
                .load(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_POSTER)))
                .error(android.R.drawable.ic_menu_report_image)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(mItemBinding.ivPoster);
    }
}
