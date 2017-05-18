package com.makkhay.androiddictionary.database;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseConfig extends SQLiteAssetHelper {

    private static final String DATABASE_NAMES = "final.sqlite";
    private static final int DATABASE_VERSION = 2;

    public DatabaseConfig(Context context) {
        super(context, DATABASE_NAMES, null, DATABASE_VERSION);
    }
}
