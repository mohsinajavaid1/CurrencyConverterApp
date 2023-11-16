package com.mohsinajavaid.currencyapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class NetworkUtils {


    public static final String CANCELED = "Canceled";
    public static final String SOCKET = "Socket";

    private Context context;

    @Inject
    public NetworkUtils(@ApplicationContext Context pContext) {
        this.context = pContext;
    }

    public boolean isConnectedToInternet() {

        boolean connected = false;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        }
        return connected;
    }
}


