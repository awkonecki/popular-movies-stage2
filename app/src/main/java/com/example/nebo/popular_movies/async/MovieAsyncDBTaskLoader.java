package com.example.nebo.popular_movies.async;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

public class MovieAsyncDBTaskLoader extends AsyncTaskLoader<Cursor> {

    private final Bundle mArgs;

    public MovieAsyncDBTaskLoader(Context context, Bundle args) {
        super(context);
        this.mArgs = args;
    }

    @Override
    public Cursor loadInBackground() {
        return null;
    }
}
