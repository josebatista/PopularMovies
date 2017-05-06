package com.example.jpereira.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by jpereira on 03/03/17.
 */

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    // TODO PUT YOUR API KEY HERE
    private static final String API_KEY = "";

    private static final String PARAM_API_KEY = "api_key";


    public static URL buildUrl(String parameter) throws MalformedURLException {

        Uri builtUri = Uri.parse(BASE_URL + parameter).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        String ret = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                ret = scanner.next();
            }

        } catch (UnknownHostException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return ret;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
