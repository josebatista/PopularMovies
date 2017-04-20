package com.example.jpereira.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jpereira.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry;

/**
 * Created by jpereira on 19/04/17.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popular_movie.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                FavoriteMovieEntry.TABLE_NAME + " ( " +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMovieEntry.MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteMovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieEntry.MOVIE_POSTER + " TEXT NOT NULL, " +
                FavoriteMovieEntry.MOVIE_SYNOPSIS + " TEXT NOT NULL, " +
                FavoriteMovieEntry.MOVIE_RATING + " TEXT NOT NULL, " +
                FavoriteMovieEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
