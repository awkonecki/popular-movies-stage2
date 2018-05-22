package com.example.nebo.popular_movies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {

    private static MovieDBHelper mMovieDBHelper = null;
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
        MovieContentProvider.mMovieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        // Obtain the descriptor reference to the SQLite database.
        final SQLiteDatabase database = MovieContentProvider.mMovieDBHelper.getReadableDatabase();
        int match = MovieContentProvider.sUriMatcher.match(uri);

        Cursor cursor = null;

        switch(match) {
            case MovieContentProvider.MOVIES:
                // retrieve all movies
                cursor = database.query(MovieContract.PATH_FAVORITE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Un-supported Uri operation.");
        }

        // Notification by the cursor to the resolver.
        ContentResolver resolver = getContext().getContentResolver();

        if (cursor != null && resolver != null) {
            cursor.setNotificationUri(resolver, uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Obtain the descriptor reference to the SQLite databse.
        final SQLiteDatabase database = MovieContentProvider.mMovieDBHelper.getWritableDatabase();
        int match = MovieContentProvider.sUriMatcher.match(uri);

        Uri resultUri = null;

        switch(match) {
            case MovieContentProvider.MOVIES:
                long id = database.insert(MovieContract.PATH_FAVORITE_MOVIES,
                        null,
                        values);

                if (id == -1) {
                    throw new SQLException(
                            "Could not insert values into the Favorite Movies table."
                    );
                }
                else {
                    resultUri = Uri.withAppendedPath(MovieContract.MovieEntry.CONTENT_URI,
                            Long.toString(id));
                }

                break;
            default:
                throw new java.lang.UnsupportedOperationException("Un-supported Uri operation.");
        }

        // Need to notify the resolver.
        ContentResolver resolver = getContext().getContentResolver();

        if (resolver != null) {
            resolver.notifyChange(resultUri, null);
        }

        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = MovieContentProvider.sUriMatcher.match(uri);

        switch(match) {
            case MovieContentProvider.MOVIES:
                break;
            case MovieContentProvider.MOVIES_WITH_ID:
                break;
            default:
                break;
        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("The update method is currently not supported.");
    }
}
