package com.example.nebo.popular_movies.async;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.nebo.popular_movies.R;
import com.example.nebo.popular_movies.data.Movie;
import com.example.nebo.popular_movies.data.MovieContract;

public class MovieAsyncDBTaskLoader extends AsyncTaskLoader<Cursor> {

    // the argument provided will need to indicate the action the resolver will need to take on the
    // content provider.
    private final Bundle mArgs;

    public MovieAsyncDBTaskLoader(Context context, Bundle args) {
        super(context);
        this.mArgs = args;
    }

    @Override
    public Cursor loadInBackground() {
        ContentResolver resolver = getContext().getContentResolver();
        Resources resources = getContext().getResources();
        Cursor cursor = null;

        if (this.mArgs != null) {
            String action = this.mArgs.getString(resources.getString(R.string.bk_db_task_action));

            if (action == null) {
                return null;
            }

            if (action.equals(resources.getString(R.string.bv_db_task_action_query))) {
                Movie movie = this.mArgs.getParcelable(resources.getString(R.string.bk_movie));

                if (movie != null) {
                    cursor = resolver.query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                            new String[]{Integer.toString(movie.getId())},
                            null,
                            null);
                }
                else {
                    cursor = resolver.query(
                            MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null,
                            null);
                }
            }
            else if (action.equals(resources.getString(R.string.bv_db_task_action_insert))) {
                Movie movie = this.mArgs.getParcelable(resources.getString(R.string.bk_movie));
                ContentValues values = new ContentValues();

                if (movie != null) {
                    // Setup the content values that of which the insertion operation will act upon.
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, movie.getPosterPath());
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKGROUND, movie.getBackdropPath());
                    values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getVote());
                    values.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, movie.getOverview());

                    Uri uri = resolver.insert(MovieContract.MovieEntry.CONTENT_URI, values);

                    if (uri == null) {
                        Log.e("Insertion Operation Failed", "Null Uri.");
                    }
                }
            }
            else if (action.equals(resources.getString(R.string.bv_db_task_action_delete))) {
                Movie movie = this.mArgs.getParcelable(resources.getString(R.string.bk_movie));

                if (movie != null) {
                    long numberOfDeletions = resolver.delete(
                            MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                                    "=" +
                                    Integer.toString(movie.getId()),
                            null);

                    if (numberOfDeletions != 1) {
                        Log.e("Deletion Operation Failed", "Deletions that occurred " +
                                Long.toString(numberOfDeletions));
                    }
                }

            }
            else {
                throw new java.lang.UnsupportedOperationException(
                        "Content resolver operation not supported"
                );
            }
        }

        return cursor;
    }
}
