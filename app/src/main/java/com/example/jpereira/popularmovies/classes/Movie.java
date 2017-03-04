package com.example.jpereira.popularmovies.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpereira on 02/03/17.
 */

public class Movie implements Parcelable {

    public static final String EXTRA_NAME = "MOVIE";

    private String mOriginalTitle;
    private String mImageUrl;
    private String mSynopsis;
    private double mRating;
    private String mReleaseDate;

    public Movie(String mOriginalTitle, String mImageUrl, String mSynopsis, double mRating, String mReleaseDate) {
        this.mOriginalTitle = mOriginalTitle;
        this.mImageUrl = mImageUrl;
        this.mSynopsis = mSynopsis;
        this.mRating = mRating;
        this.mReleaseDate = mReleaseDate;
    }

    public Movie(Parcel in) {
        mOriginalTitle = in.readString();
        mImageUrl = in.readString();
        mSynopsis = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readString();
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public void setmSynopsis(String mSynopsis) {
        this.mSynopsis = mSynopsis;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mImageUrl);
        dest.writeString(mSynopsis);
        dest.writeDouble(mRating);
        dest.writeString(mReleaseDate);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mRating=" + mRating +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                '}';
    }
}