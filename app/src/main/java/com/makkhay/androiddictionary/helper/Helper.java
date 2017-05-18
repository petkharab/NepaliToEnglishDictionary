package com.makkhay.androiddictionary.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Helper {

    public static String SEARCH_WORD = "SEARCH_WORD";
    public static String WIKI_CONTENT = "Wiki word definition";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
