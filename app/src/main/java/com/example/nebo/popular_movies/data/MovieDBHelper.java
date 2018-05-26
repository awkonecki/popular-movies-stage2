package com.example.nebo.popular_movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.nebo.popular_movies.data.MovieContract.MovieEntry;

/**
 * @brief Helper class that provides the necessary definition of the `onCreate` & `onUpdate` methods
 * associated with the contract for the `popular-movies` application.
 */
public class MovieDBHelper extends SQLiteOpenHelper {
    private static final String SQLITE_FILE_NAME = "favorite-movies.db";
    private static final int SQLITE_VERSION_NUMBER = 1;

    public MovieDBHelper(Context context) {
        super(context,
                MovieDBHelper.SQLITE_FILE_NAME,
                null,
                MovieDBHelper.SQLITE_VERSION_NUMBER,
                null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Need to actually build the SQLite statement to build the table.
        final String SQL_STATEMENT = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL " +
                ");";

        // Execute the SQL statement to actually build the table.
        db.execSQL(SQL_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now just dump the table.  Will later want to append new information and set a default
        // for already existing entries.
        // @TODO
    }
}
