package com.example.jpereira.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jpereira.popularmovies.R;
import com.example.jpereira.popularmovies.classes.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jpereira on 02/03/17.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(@NonNull Context context, @NonNull List<Movie> movieList) {
        super(context, 0, movieList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie mMovie = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_grid_view_item, parent, false);
        }

        ImageView mPoster = (ImageView) convertView.findViewById(R.id.iv_poster);
        Picasso.with(getContext()).load(mMovie.getmImageUrl()).into(mPoster);

        return convertView;
    }
}
