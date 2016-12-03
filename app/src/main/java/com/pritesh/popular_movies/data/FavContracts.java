package com.pritesh.popular_movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by prittesh on 24/11/16.
 */

public class FavContracts {

    public static final String CONTENT_AUTHORITY = "com.pritesh.popular_movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavEntry implements BaseColumns {

        public static final String TABLE_FAV="fav";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAV).build();

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_IMAGE="image";
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAV;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAV;
        public static Uri buildFavUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
