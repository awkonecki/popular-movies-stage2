package com.example.nebo.popular_movies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {

    private MovieDBHelper movieDBHelper = null;
    private static final UriMatcher sUriMatcher = MovieContentProvider.buildUriMatcher();

    // Building URI matcher to define the set of Uri expressions that are acceptable with the
    // content provider for the Movie database.
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add matches that the UriMatcher is needed to support.
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_FAVORITE_MOVIES,
                MovieContentProvider.MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_FAVORITE_MOVIES + "/#",
                MovieContentProvider.MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        // Setup the helper for the content provider to interact with.
        this.movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
