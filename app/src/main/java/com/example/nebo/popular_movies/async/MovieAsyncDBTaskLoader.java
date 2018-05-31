package com.example.nebo.popular_movies.async;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;

import com.example.nebo.popular_movies.R;

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
                cursor = resolver.query(null,
                        null, null,
                        null, null,
                        null);
            }
            else if (action.equals(resources.getString(R.string.bv_db_task_action_insert))) {
                resolver.insert(null, null);
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
