package com.pritesh.popular_movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FavProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mDBHelper;

    private static final int FAV = 100;
    private static final int FAV_WITH_ID = 200;

    public FavProvider() {
    }

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavContracts.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, FavContracts.FavEntry.TABLE_FAV, FAV);
        matcher.addURI(authority, FavContracts.FavEntry.TABLE_FAV + "/#", FAV_WITH_ID);

        return matcher;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case FAV:
                numDeleted = db.delete(
                        FavContracts.FavEntry.TABLE_FAV, selection, selectionArgs);
                // reset _ID
//                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
//                        FavContracts.FavEntry.TABLE_FAV + "'");
                break;
            case FAV_WITH_ID:
                numDeleted = db.delete(  FavContracts.FavEntry.TABLE_FAV,
                        FavContracts.FavEntry.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
//                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
//                        FavContracts.FavEntry.TABLE_FAV + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FAV:{
                return FavContracts.FavEntry.CONTENT_DIR_TYPE;
            }
            case FAV_WITH_ID:{
                return FavContracts.FavEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        // at the given URI.
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FAV: {
                long _id = db.insert(FavContracts.FavEntry.TABLE_FAV, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = FavContracts.FavEntry.buildFavUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        mDBHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            // All Flavors selected
            case FAV:{
                retCursor =mDBHelper.getReadableDatabase().query(
                        FavContracts.FavEntry.TABLE_FAV,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            // Individual flavor based on Id selected
            case FAV_WITH_ID:{
                retCursor = mDBHelper.getReadableDatabase().query(
                        FavContracts.FavEntry.TABLE_FAV,
                        projection,
                        FavContracts.FavEntry.COLUMN_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int numUpdated = 0;

        if (values == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case FAV:{
                numUpdated = db.update(FavContracts.FavEntry.TABLE_FAV,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FAV_WITH_ID: {
                numUpdated = db.update(FavContracts.FavEntry.TABLE_FAV,
                        values,
                        FavContracts.FavEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
