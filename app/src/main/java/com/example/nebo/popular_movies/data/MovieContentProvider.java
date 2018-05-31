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

    /**
     * @note Logic follows similar to that provided by Udacity from Lesson 11.31
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MovieContentProvider.MOVIES:
                // directory
                return "vnd.android.cursor.dir" + "/" +
                        MovieContract.CONTENT_AUTHORITY + "/" +
                        MovieContract.PATH_FAVORITE_MOVIES;
            case MovieContentProvider.MOVIES_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" +
                        MovieContract.CONTENT_AUTHORITY + "/" +
                        MovieContract.PATH_FAVORITE_MOVIES + "/#";
            default:
                throw new UnsupportedOperationException(
                        "Uri not supported " + uri
                );
        }
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
            case MovieContentProvider.MOVIES_WITH_ID:
                break;
            default:
                throw new UnsupportedOperationException("Un-supported Uri operation.");
        }

        // Notification by the cursor to the resolver.
        ContentResolver resolver = null;

        if (getContext() != null) {
            resolver =  getContext().getContentResolver();
        }

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

    /**
     * @brief Deletes based on the selection and selection argument parameters provided.
     * @param uri Indicates the Uri that is being targeted to determine operational support.
     * @param selection String that indicates the columns of interest
     * @param selectionArgs String that indicates the values that would be associated with the
     *                      columns.
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase database = MovieContentProvider.mMovieDBHelper.getWritableDatabase();
        int match = MovieContentProvider.sUriMatcher.match(uri);
        int deletedCount = 0;

        switch(match) {
            case MovieContentProvider.MOVIES:
                deletedCount = database.delete(MovieContract.PATH_FAVORITE_MOVIES,
                        selection,
                        selectionArgs);
                break;
            case MovieContentProvider.MOVIES_WITH_ID:
                deletedCount = database.delete(MovieContract.PATH_FAVORITE_MOVIES,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        "The delete method does not support the intented Uri operation."
                );
        }

        // Need to notify the resolver to reflect the removal if necessary.
        ContentResolver resolver = getContext().getContentResolver();

        if (deletedCount > 0 && resolver != null) {
            resolver.notifyChange(uri, null);
        }

        return deletedCount;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException(
                "The update method is currently not supported."
        );
    }
}
