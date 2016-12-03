package com.pritesh.popular_movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by prittesh on 25/11/16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;
SQLiteDatabase db;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                FavContracts.FavEntry.TABLE_FAV + "(" + FavContracts.FavEntry._ID +
                " TEXT PRIMARY KEY , " +
                FavContracts.FavEntry.COLUMN_DATE + " TEXT , " +
                FavContracts.FavEntry.COLUMN_TITLE +
                " TEXT , " + FavContracts.FavEntry.COLUMN_RATING +
                " TEXT , " + FavContracts.FavEntry.COLUMN_IMAGE +
                " TEXT , "+FavContracts.FavEntry.COLUMN_SUMMARY +
                " TEXT );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  FavContracts.FavEntry.TABLE_FAV);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                 FavContracts.FavEntry.TABLE_FAV + "'");
        // re-create database
        onCreate(sqLiteDatabase);
    }
}
