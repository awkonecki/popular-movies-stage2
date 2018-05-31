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
                cursor = resolver.query(MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null,
                        null);
                if (cursor != null) {
                    Log.d("Query Operation", Integer.toString(cursor.getCount()));
                    cursor.close();
                }
            }
            else if (action.equals(resources.getString(R.string.bv_db_task_action_insert))) {
                Movie movie = this.mArgs.getParcelable(resources.getString(R.string.bk_movie));
                ContentValues values = new ContentValues();

                Log.d("Attempting Assertion", "Attempting assertion");

                if (movie != null) {
                    // Setup the content values that of which the insertion operation will act upon.
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());

                    Uri uri = resolver.insert(MovieContract.MovieEntry.CONTENT_URI, values);

                    if (uri != null) {
                        Log.d("Insertion Operation", uri.toString());
                    }
                }
            }
            else if (action.equals(resources.getString(R.string.bv_db_task_action_delete))) {
                resolver.delete(null, null, null);
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
