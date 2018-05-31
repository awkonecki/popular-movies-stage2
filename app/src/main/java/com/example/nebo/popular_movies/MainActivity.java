package com.example.nebo.popular_movies;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.nebo.popular_movies.async.MovieAsyncDBTaskLoader;
import com.example.nebo.popular_movies.data.Movie;
import com.example.nebo.popular_movies.databinding.ActivityMainBinding;
import com.example.nebo.popular_movies.async.MovieAsyncTaskLoader;
import com.example.nebo.popular_movies.async.MovieManagedData;
import com.example.nebo.popular_movies.util.JsonUtils;
import com.example.nebo.popular_movies.views.MoviePosterViewHolder;

public class MainActivity extends AppCompatActivity implements
        AppAdapter.AppAdapterOnClickListener {

    private static ActivityMainBinding sBinding = null;

    private static boolean mLoading = false;
    private static final int FETCH_DATA_ID = 14;
    private static final int POPULAR_MODE = 0;
    private static final int TOP_RATED_MODE = 1;
    private static final int FAVORITE_MODE = 2;
    private static final int DB_QUERY_TASK = 3;
    private static final int DEFAULT_MODE = MainActivity.POPULAR_MODE;

    private AppAdapter<Movie, MoviePosterViewHolder<Movie>> mMovieAdapter = null;
    private static Menu mMenu = null;

    private static MovieManagedData mPopularMovies = null;
    private static MovieManagedData mTopRatedMovies = null;
    private static MovieManagedData mFavoriteMovies = null;
    private static int mMode = MainActivity.DEFAULT_MODE;

    private MovieManagedData mActiveData = null;

    /**
     * @brief Set the UI element visibility during the fetch.
     */
    private void onFetch() {
        MainActivity.sBinding.pbMainProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * @brief Set the UI element visibility after a fetch operation.
     */
    private void fetchComplete() {
        MainActivity.sBinding.pbMainProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * @brief Fetch the data from the appropriate source based on the current active data set.
     * @TODO Account for each type of data-set.  Currently only processing for popular.
     */
    private void fetchData() {
        Bundle args;

        if (!MainActivity.mLoading) {
            args = new Bundle();
            args.putInt(getString(R.string.bk_page_number), this.mActiveData.getPage());
            args.putString(getString(R.string.bk_request_type), this.mActiveData.getType());

            MainActivity.mLoading = true;
            // Loader Manager for async tasks
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> movieLoader = loaderManager.getLoader(MainActivity.FETCH_DATA_ID);

            // Set the visibility due to fetch action being started.
            this.onFetch();

            if (movieLoader == null) {
                loaderManager.initLoader(MainActivity.FETCH_DATA_ID, args, new NetworkAsyncTaskLoader()).forceLoad();
            } else {
                loaderManager.restartLoader(MainActivity.FETCH_DATA_ID, args, new NetworkAsyncTaskLoader()).forceLoad();
            }
        }
    }

    private void queryData() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(MainActivity.DB_QUERY_TASK);

        Bundle args = new Bundle();
        args.putString(getString(R.string.bk_db_task_action),
                getString(R.string.bv_db_task_action_query));

        if (loader == null) {
            loaderManager.initLoader(MainActivity.DB_QUERY_TASK,
                    args,
                    new DataBaseAsyncTaskLoader()
            ).forceLoad();
        }
        else {
            loaderManager.restartLoader(MainActivity.DB_QUERY_TASK,
                    args,
                    new DataBaseAsyncTaskLoader()
            ).forceLoad();
        }
    }

    /**
     * @brief set the class's instance of mActiveData member to the desired data set to operate on.
     */
    private void setCurrentMovieData() {
        // For the time being not going to deal with the race conidition of the mode being changed
        // with a pending background network request.
        switch(MainActivity.mMode) {
            case MainActivity.POPULAR_MODE:
                this.mActiveData = MainActivity.mPopularMovies;
                break;
            case MainActivity.TOP_RATED_MODE:
                this.mActiveData = MainActivity.mTopRatedMovies;
                break;
            case MainActivity.FAVORITE_MODE:
                this.mActiveData = MainActivity.mFavoriteMovies;
                break;
            default:
                break;
        }
    }

    /**
     * @brief Sets the data for the recyclerviewer adapter instance and attempts to set the correct
     * visible element.
     * @TODO the visible piece is going back to the initial upon selection changes.  Is this due to
     * the mAdapter when it populates the views setting it back to zero???
     */
    private void setView() {
        GridLayoutManager gridLayoutManager =
                (GridLayoutManager) MainActivity.sBinding.rvRecyclerView.getLayoutManager();

        // Manage the view if the instance state exists.
        if (this.mActiveData.getMovies().size() > 0) {
            this.mMovieAdapter.setAdapterData(this.mActiveData.getMovies());
            gridLayoutManager.scrollToPosition(this.mActiveData.getFirstVisible());
        }
        else {
            // Attempt to fetch data.
            this.fetchData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.sBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Setup of the recycler view for the main activity.
        // 1. Create an adapter.
        mMovieAdapter = new AppAdapter<Movie, MoviePosterViewHolder<Movie>>(
                this,
                R.layout.grid_item
        );

        // 3. Make a new LayoutManager of the `GridLayout` type.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2, GridLayoutManager.VERTICAL, false);

        // 4. Set the properties that the recycler viewer wil use.
        MainActivity.sBinding.rvRecyclerView.setAdapter(mMovieAdapter);
        MainActivity.sBinding.rvRecyclerView.setLayoutManager(gridLayoutManager);
        MainActivity.sBinding.rvRecyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {
            MainActivity.mPopularMovies =
                    savedInstanceState.getParcelable(getString(R.string.bsik_popular));
            MainActivity.mTopRatedMovies =
                    savedInstanceState.getParcelable(getString(R.string.bsik_top_rated));
            MainActivity.mMode =
                    savedInstanceState.getInt(getString(R.string.bsik_mode),
                            MainActivity.DEFAULT_MODE);
        }
        else {
            mPopularMovies = new MovieManagedData(getString(R.string.bv_request_type_popular));
            mTopRatedMovies = new MovieManagedData(getString(R.string.bv_request_type_top_rated));
        }

        // Set the current active movie data.
        setCurrentMovieData();

        // Set the view with the correct movie data.
        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MainActivity.mMenu = menu;

        // Need to set the correct UI mode for the filter.
        if (MainActivity.mMode == MainActivity.TOP_RATED_MODE) {
            MenuItem item = null;
            item = menu.findItem(R.id.menu_item_sort_popular);
            item.setChecked(false);
            item = menu.findItem(R.id.menu_item_sort_top_rated);
            item.setChecked(true);
            item = MainActivity.mMenu.findItem(R.id.menu_item_sort_favorites);
            item.setChecked(false);
        }
        else if (MainActivity.mMode == MainActivity.POPULAR_MODE) {
            MenuItem item = null;
            item = menu.findItem(R.id.menu_item_sort_popular);
            item.setChecked(true);
            item = menu.findItem(R.id.menu_item_sort_top_rated);
            item.setChecked(false);
            item = MainActivity.mMenu.findItem(R.id.menu_item_sort_favorites);
            item.setChecked(false);
        }
        else {
            MenuItem item = null;
            item = menu.findItem(R.id.menu_item_sort_popular);
            item.setChecked(false);
            item = menu.findItem(R.id.menu_item_sort_top_rated);
            item.setChecked(false);
            item = MainActivity.mMenu.findItem(R.id.menu_item_sort_favorites);
            item.setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();

        switch (selectedItemId) {
            case R.id.menu_item_sort_popular:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    MenuItem menuItem = MainActivity.mMenu.findItem(R.id.menu_item_sort_top_rated);
                    menuItem.setChecked(false);
                    menuItem = MainActivity.mMenu.findItem(R.id.menu_item_sort_favorites);
                    menuItem.setChecked(false);
                    MainActivity.mMode = MainActivity.POPULAR_MODE;
                    this.setCurrentMovieData();
                    this.setView();
                    this.setTitle(getString(R.string.popular_title));
                }
                break;
            case R.id.menu_item_sort_top_rated:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    MenuItem menuItem = MainActivity.mMenu.findItem(R.id.menu_item_sort_popular);
                    menuItem.setChecked(false);
                    menuItem = MainActivity.mMenu.findItem(R.id.menu_item_sort_favorites);
                    menuItem.setChecked(false);
                    MainActivity.mMode = MainActivity.TOP_RATED_MODE;
                    this.setCurrentMovieData();
                    this.setView();
                    this.setTitle(getString(R.string.top_rated_title));
                }
                break;
            case R.id.menu_item_sort_favorites:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    MenuItem menuItem = MainActivity.mMenu.findItem(R.id.menu_item_sort_popular);
                    menuItem.setChecked(false);
                    menuItem = MainActivity.mMenu.findItem(R.id.menu_item_sort_top_rated);
                    menuItem.setChecked(false);
                    MainActivity.mMode = MainActivity.FAVORITE_MODE;
                    // query movies.
                    this.queryData();
                    this.setCurrentMovieData();
                    // this.setView();
                    this.setTitle(getString(R.string.favorite_title));
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", this.mActiveData.getMovies().get(position));

        startActivity(intent);
    }

    //**********************************************************************************************
    // START ANDROID LIFE-CYCLE METHODS
    //**********************************************************************************************
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current stack of movies already queried.
        outState.putParcelable(getString(R.string.bsik_popular), MainActivity.mPopularMovies);
        outState.putParcelable(getString(R.string.bsik_top_rated), MainActivity.mTopRatedMovies);
        outState.putInt(getString(R.string.bsik_mode), mMode);
    }

    //**********************************************************************************************
    // END ANDROID LIFE-CYCLE METHODS
    //**********************************************************************************************

    /**
     * @class NetworkAsyncTaskLoader
     * @brief LoaderManager definition for performing network async tasks for the main activity.
     */
    private class NetworkAsyncTaskLoader implements LoaderManager.LoaderCallbacks<String> {
        @NonNull
        @Override
        public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
            switch(id) {
                case MainActivity.FETCH_DATA_ID:
                    return new MovieAsyncTaskLoader(MainActivity.this, args);
                default:
                    throw new java.lang.IllegalArgumentException("Unsupported ID value.");
            }
        }

        @Override
        public void onLoadFinished(@NonNull Loader<String> loader, String response) {
            // Only process if response contains content.
            if (response != null && !response.isEmpty()) {
                // Add the list of movies to the overall list.
                MainActivity.this.mActiveData.addMovies(JsonUtils.parseJsonResponseForMovies(response));

                // Inform the movie adapter of the change.
                MainActivity.this.mMovieAdapter.setAdapterData(MainActivity.this.mActiveData.getMovies());
                MainActivity.this.mActiveData.incrementPage();
            }

            // Ensure that the loading progress bar is made invisible.
            MainActivity.this.fetchComplete();

            // Ensure that more loading of data can occur.
            MainActivity.mLoading = false;
        }

        @Override
        public void onLoaderReset(@NonNull Loader<String> loader) { }
    }

    /**
     * @class DataBaseAsyncTaskLoader
     * @brief LoaderManager definition for performing database async tasks for the main activity.
     */
    private class DataBaseAsyncTaskLoader implements LoaderManager.LoaderCallbacks<Cursor> {

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            Loader<Cursor> loader = null;
            switch (id) {
                case MainActivity.DB_QUERY_TASK:
                    loader = new MovieAsyncDBTaskLoader(MainActivity.this, args);
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Invalid DB Query Task ID - MainActivity"
                    );
            }
            return loader;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            switch (loader.getId()) {
                case MainActivity.DB_QUERY_TASK:
                    if (data != null) {
                        Log.d("Query Size", Integer.toString(data.getCount()));
                        data.close();
                    }
                    break;
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) { }
    }

    //**********************************************************************************************
    // END LOADER METHODS FOR ASYNC TASKS
    //**********************************************************************************************
}
