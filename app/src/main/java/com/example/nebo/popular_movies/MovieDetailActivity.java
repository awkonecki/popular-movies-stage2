package com.example.nebo.popular_movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.nebo.popular_movies.async.MovieAsyncTaskLoader;
import com.example.nebo.popular_movies.databinding.MovieDetailContentBinding;
import com.example.nebo.popular_movies.data.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private MovieDetailContentBinding mDetailBinding = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        this.mDetailBinding = DataBindingUtil.setContentView(this,
                R.layout.movie_detail_content);

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
        // @TODO Provide a listener for the trailer (implicit intent).
        AppAdapter<String> reviewAdapter = new AppAdapter<>(null,
                R.layout.movie_review_item);
        AppAdapter<String> trailerAdapter = new AppAdapter<>(null,
                R.layout.movie_trailer_item);

        // 3. Adding of the layout manager to the recycler views.
        this.mDetailBinding.rvMovieDetailReviews.setLayoutManager(reviewLayoutManager);
        this.mDetailBinding.rvMovieDetailTrailers.setLayoutManager(trailerLayoutManager);

        // 4. Adding of the adapters to the recycler views.
        this.mDetailBinding.rvMovieDetailReviews.setAdapter(reviewAdapter);
        this.mDetailBinding.rvMovieDetailTrailers.setAdapter(trailerAdapter);

        // 5. Set settings for recycler viewer.
        this.mDetailBinding.rvMovieDetailReviews.setHasFixedSize(true);
        this.mDetailBinding.rvMovieDetailTrailers.setHasFixedSize(true);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        Movie movie = null;

        if (intent != null) {
            movie = intent.getParcelableExtra("movie");
        }

        this.populateUI(movie);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> reviewTrailerLoader = loaderManager.getLoader(1);

        if (loaderManager == null) {
            Log.d("LoaderManagerInfo", "Null manager");
        }

        Bundle args = new Bundle();
        args.putInt(getString(R.string.bk_page_number), 1);
        args.putString(getString(R.string.bk_request_type), getString(R.string.bv_request_type_reviews));
        args.putInt(getString(R.string.bk_movie_id), movie.getId());

        if (reviewTrailerLoader == null) {
            loaderManager.initLoader(1, args, this).forceLoad();
        }
        else {
            loaderManager.restartLoader(1, args, this).forceLoad();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MovieDetailActivity", "onSaveInstaceState called");
    }

    private void populateUI(Movie movie) {
        String title = null;

        if (this.mDetailBinding == null) {
            Log.d("Populate UI", "null bindings : ");
            return;
        }

        if (movie != null) {
            Picasso.get().load(movie.getPosterPath()).error(R.drawable.image_placeholder).
                    into(this.mDetailBinding.ivMoivePosterDetail);
            // Picasso.get().load(movie.getBackdropPath()).error(R.drawable.image_placeholder).
            //        into(this.mMovieBinding.ivBackgroundDetail);

            this.setTitle(movie.getTitle());
            this.mDetailBinding.tvMovieDescription.setText(movie.getOverview());
            this.mDetailBinding.tvMovieDetailReleaseDate.setText(movie.getReleaseDate());
            this.mDetailBinding.tvMovieDetailRating.setText(Double.toString(movie.getVote()));
            this.mDetailBinding.tvMovieDetailTitle.setText(movie.getTitle());
        }
    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        android.support.v4.content.Loader<String> loader;
        switch(id) {
            case 1:
                loader = new MovieAsyncTaskLoader(this, args);
                break;
            default:
                throw new IllegalArgumentException("Invalid ID");
        }

        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<String> loader, String data) {
        Log.d("onLoadFinished", data);
        AppAdapter<String> reviewAdapter = (AppAdapter<String>) this.mDetailBinding.rvMovieDetailReviews.getAdapter();
        // reviewAdapter.setAdapterData();
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<String> loader) {

    }
}
