package com.example.nebo.popular_movies;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.nebo.popular_movies.async.MovieAsyncDBTaskLoader;
import com.example.nebo.popular_movies.async.MovieAsyncTaskLoader;
import com.example.nebo.popular_movies.data.Review;
import com.example.nebo.popular_movies.data.Trailer;
import com.example.nebo.popular_movies.databinding.MovieDetailBinding;
import com.example.nebo.popular_movies.data.Movie;
import com.example.nebo.popular_movies.util.JsonUtils;
import com.example.nebo.popular_movies.util.MovieURLUtils;
import com.example.nebo.popular_movies.views.MovieReviewViewHolder;
import com.example.nebo.popular_movies.views.MovieTrailerViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {

    private static Movie mMovie = null;
    private MovieDetailBinding mDetailBinding = null;
    private AppAdapter<Review, MovieReviewViewHolder<Review>> mReviewAdapter = null;
    private AppAdapter<Trailer, MovieTrailerViewHolder<Trailer>> mTrailerAdapter = null;
    private static final int REVIEW_TASK = 1;
    private static final int TRAILER_TASK = 2;
    private static final int FAVORITE_TASK = 3;
    private static final int UNFAVORITE_TASK = 4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.movie_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        switch(selected) {
            case R.id.menu_item_set_favorite_status:
                if (item.getTitle().equals(getString(R.string.mi_favorite))) {
                    item.setTitle(getString(R.string.mi_unfavorite));
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);

                    this.setMovieAsFavorite();
                }
                else {
                    item.setTitle(getString(R.string.mi_favorite));
                    item.setIcon(R.drawable.ic_favorite_red_24dp);

                    this.removeMovieAsFavorite();
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.movie_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 1. Creation of the layout managers for the reviews and trailers.
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);

        // 2. Creation of the adapters for the recycler views.
        this.mReviewAdapter =
                new AppAdapter<Review, MovieReviewViewHolder<Review>>(null,
                R.layout.movie_review_item);
        this.mTrailerAdapter =
                new AppAdapter<Trailer, MovieTrailerViewHolder<Trailer>>(new MovieTrailerPlayer(),
                R.layout.movie_trailer_item);

        // 3. Adding of the layout manager to the recycler views.
        this.mDetailBinding.movieDetail.rvMovieDetailReviews.setLayoutManager(reviewLayoutManager);
        this.mDetailBinding.movieDetail.rvMovieDetailTrailers.setLayoutManager(trailerLayoutManager);

        // 4. Adding of the adapters to the recycler views.
        this.mDetailBinding.movieDetail.rvMovieDetailReviews.setAdapter(this.mReviewAdapter);
        this.mDetailBinding.movieDetail.rvMovieDetailTrailers.setAdapter(this.mTrailerAdapter);

        // 5. Set settings for recycler viewer.
        this.mDetailBinding.movieDetail.rvMovieDetailReviews.setHasFixedSize(true);
        this.mDetailBinding.movieDetail.rvMovieDetailTrailers.setHasFixedSize(true);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent == null) {
                finish();
            }

            MovieDetailActivity.mMovie = null;

            if (intent != null) {
                MovieDetailActivity.mMovie = intent.getParcelableExtra("movie");
            }

            this.obtainTrailers();
            this.obtainReviews();
        }
        else {
            MovieDetailActivity.mMovie = savedInstanceState.
                    getParcelable(getString(R.string.bk_movie));
            this.mTrailerAdapter.setAdapterData(savedInstanceState.
                    <Trailer>getParcelableArrayList(getString(R.string.bk_trailers)));
            this.mReviewAdapter.setAdapterData(savedInstanceState.
                    <Review>getParcelableArrayList(getString(R.string.bk_reviews)));
        }

        this.populateUI();

        // @TODO Check to see if the movie is a favorite of the user.
        // @TODO Set the menu item context appropriately.
        android.support.v4.app.LoaderManager loaderManager = this.getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(MovieDetailActivity.FAVORITE_TASK);

        if (loader == null) {
            // loaderManager.initLoader(MovieDetailActivity.FAVORITE_TASK, null, new DatabaseAsyncLoader()).forceLoad();
        }
        else {
            // loaderManager.restartLoader(MovieDetailActivity.FAVORITE_TASK, null, new DatabaseAsyncLoader()).forceLoad();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.bk_movie), MovieDetailActivity.mMovie);
        outState.putParcelableArrayList(getString(R.string.bk_reviews),
                (ArrayList<Review>) this.mReviewAdapter.getAdapterData());
        outState.putParcelableArrayList(getString(R.string.bk_trailers),
                (ArrayList<Trailer>) this.mTrailerAdapter.getAdapterData());
    }

    private class NetworkAsyncLoader implements LoaderManager.LoaderCallbacks<String> {
        @NonNull
        @Override
        public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
            Loader<String> loader = null;

            switch (id) {
                case MovieDetailActivity.REVIEW_TASK:
                case MovieDetailActivity.TRAILER_TASK:
                    loader = new MovieAsyncTaskLoader(MovieDetailActivity.this, args);
                    break;
                default:
                    throw new UnsupportedOperationException("Illegal id.");
            }

            return loader;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<String> loader, String data) {
            switch (loader.getId()) {
                case MovieDetailActivity.REVIEW_TASK:
                    if (data != null) {
                        MovieDetailActivity.this.mReviewAdapter.
                                setAdapterData(JsonUtils.parseJsonResponseForReviews(data));
                    }
                    break;
                case MovieDetailActivity.TRAILER_TASK:
                    if (data != null) {
                        MovieDetailActivity.this.mTrailerAdapter.
                                setAdapterData(JsonUtils.parseJsonResponseForTrailers(data));
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Illegal id.");
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<String> loader) { }
    }

    private class DatabaseAsyncLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Loader<Cursor> loader = null;

            switch (id) {
                case MovieDetailActivity.FAVORITE_TASK:
                    loader = new MovieAsyncDBTaskLoader(MovieDetailActivity.this, args);
                    break;
                case MovieDetailActivity.UNFAVORITE_TASK:
                    loader = new MovieAsyncDBTaskLoader(MovieDetailActivity.this, args);
                    break;
                default:
                    throw new UnsupportedOperationException("Illegal id.");
            }

            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) { }
    }

    private void setMovieAsFavorite() {
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.bk_movie), MovieDetailActivity.mMovie);
        args.putString(getString(R.string.bk_db_task_action),
                getString(R.string.bv_db_task_action_insert));
        this.startDataBaseLoaderTask(MovieDetailActivity.FAVORITE_TASK, args);
    }

    private void removeMovieAsFavorite() {
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.bk_movie), MovieDetailActivity.mMovie);
        args.putString(getString(R.string.bk_db_task_action),
                getString(R.string.bv_db_task_action_delete));
        this.startDataBaseLoaderTask(MovieDetailActivity.UNFAVORITE_TASK, args);
    }

    private void startDataBaseLoaderTask(int taskId, Bundle args) {
        LoaderManager loaderManager = this.getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(taskId);

        if (loader == null) {
            loaderManager.initLoader(taskId, args, new DatabaseAsyncLoader()).forceLoad();
        }
        else {
            loaderManager.restartLoader(taskId, args, new DatabaseAsyncLoader()).forceLoad();
        }
    }

    private void obtainTrailers() {
        // Build the args
        Bundle args = new Bundle();
        args.putInt(getString(R.string.bk_page_number), 1);
        args.putString(getString(R.string.bk_request_type),
                getString(R.string.bv_request_type_trailers));
        args.putInt(getString(R.string.bk_movie_id), MovieDetailActivity.mMovie.getId());

        this.startNetworkLoaderTask(MovieDetailActivity.TRAILER_TASK, args);
    }

    private void obtainReviews() {
        // Build the args
        Bundle args = new Bundle();
        args.putInt(getString(R.string.bk_page_number), 1);
        args.putString(getString(R.string.bk_request_type),
                getString(R.string.bv_request_type_reviews));
        args.putInt(getString(R.string.bk_movie_id), MovieDetailActivity.mMovie.getId());

        this.startNetworkLoaderTask(MovieDetailActivity.REVIEW_TASK, args);
    }

    private void startNetworkLoaderTask(int taskId, Bundle args) {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> reviewTrailerLoader = loaderManager.getLoader(taskId);

        if (loaderManager == null) {
            Log.d("LoaderManagerInfo", "Null manager");
        }

        if (reviewTrailerLoader == null) {
            loaderManager.initLoader(taskId, args, new NetworkAsyncLoader()).forceLoad();
        }
        else {
            loaderManager.restartLoader(taskId, args, new NetworkAsyncLoader()).forceLoad();
        }
    }

    private void populateUI() {
        String title = getString(R.string.default_title);

        if (this.mDetailBinding == null) {
            Log.d("Populate UI", "null bindings : ");
            return;
        }

        if (MovieDetailActivity.mMovie != null) {
            Picasso.get().load(MovieDetailActivity.mMovie.getPosterPath()).
                    error(R.drawable.image_placeholder).
                    into(this.mDetailBinding.movieDetail.ivMoviePosterDetail);
            Picasso.get().load(MovieDetailActivity.mMovie.getBackdropPath()).
                    error(R.drawable.image_placeholder).
                    into(this.mDetailBinding.ivBackgroundDetail);

            this.setTitle(MovieDetailActivity.mMovie.getTitle());
            this.mDetailBinding.movieDetail.tvMovieDescription.
                    setText(MovieDetailActivity.mMovie.getOverview());
            this.mDetailBinding.movieDetail.tvMovieDetailReleaseDate.
                    setText(MovieDetailActivity.mMovie.getReleaseDate());
            this.mDetailBinding.movieDetail.tvMovieDetailRating.
                    setText(Double.toString(MovieDetailActivity.mMovie.getVote()));
            this.mDetailBinding.movieDetail.tvMovieDetailTitle.
                    setText(MovieDetailActivity.mMovie.getTitle());
        }
        else {
            this.setTitle(title);
        }
    }

    private class MovieTrailerPlayer implements AppAdapter.AppAdapterOnClickListener {
        public void onClick(int position) {
            Trailer trailer = mTrailerAdapter.getAdapterDataAt(position);

            if (trailer != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("video/*");
                intent.setData(MovieURLUtils.buildVideoUri(trailer.getmKey()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        }
    }
}
