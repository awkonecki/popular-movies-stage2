package com.example.nebo.popular_movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
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
import com.example.nebo.popular_movies.data.Review;
import com.example.nebo.popular_movies.data.Trailer;
import com.example.nebo.popular_movies.databinding.MovieDetailBinding;
import com.example.nebo.popular_movies.databinding.MovieDetailContentBinding;
import com.example.nebo.popular_movies.data.Movie;
import com.example.nebo.popular_movies.util.JsonUtils;
import com.example.nebo.popular_movies.util.MovieURLUtils;
import com.example.nebo.popular_movies.views.MovieReviewViewHolder;
import com.example.nebo.popular_movies.views.MovieTrailerViewHolder;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static Movie mMovie = null;
    private MovieDetailBinding mDetailBinding = null;
    private AppAdapter<Review, MovieReviewViewHolder<Review>> mReviewAdapter = null;
    private AppAdapter<Trailer, MovieTrailerViewHolder<Trailer>> mTrailerAdapter = null;
    private static final int REVIEW_TASK = 1;
    private static final int TRAILER_TASK = 2;

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

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        Movie movie = null;

        if (intent != null) {
            movie = intent.getParcelableExtra("movie");
        }

        this.populateUI(movie);
        this.obtainTrailers(movie.getId());
        this.obtainReviews(movie.getId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        android.support.v4.content.Loader<String> loader;
        switch(id) {
            case MovieDetailActivity.REVIEW_TASK:
            case MovieDetailActivity.TRAILER_TASK:
                loader = new MovieAsyncTaskLoader(this, args);
                break;
            default:
                throw new IllegalArgumentException("Invalid ID");
        }

        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<String> loader, String data) {
        if (data != null) {
            int taskId = loader.getId();

            switch(taskId) {
                case MovieDetailActivity.REVIEW_TASK:
                    this.mReviewAdapter.setAdapterData(JsonUtils.parseJsonResponseForReviews(data));
                    break;
                case MovieDetailActivity.TRAILER_TASK:
                    this.mTrailerAdapter.setAdapterData(JsonUtils.parseJsonResponseForTrailers(data));
                    break;
                default:
                    throw new java.lang.UnsupportedOperationException("Invalid activity ID.");
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<String> loader) { }

    private void obtainTrailers(int movieID) {
        // Build the args
        Bundle args = new Bundle();
        args.putInt(getString(R.string.bk_page_number), 1);
        args.putString(getString(R.string.bk_request_type),
                getString(R.string.bv_request_type_trailers));
        args.putInt(getString(R.string.bk_movie_id), movieID);

        this.startLoaderTask(MovieDetailActivity.TRAILER_TASK, args);
    }

    private void obtainReviews(int movieID) {
        // Build the args
        Bundle args = new Bundle();
        args.putInt(getString(R.string.bk_page_number), 1);
        args.putString(getString(R.string.bk_request_type),
                getString(R.string.bv_request_type_reviews));
        args.putInt(getString(R.string.bk_movie_id), movieID);

        this.startLoaderTask(MovieDetailActivity.REVIEW_TASK, args);
    }

    private void startLoaderTask(int taskId, Bundle args) {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> reviewTrailerLoader = loaderManager.getLoader(1);

        if (loaderManager == null) {
            Log.d("LoaderManagerInfo", "Null manager");
        }

        if (reviewTrailerLoader == null) {
            loaderManager.initLoader(taskId, args, this).forceLoad();
        }
        else {
            loaderManager.restartLoader(taskId, args, this).forceLoad();
        }
    }

    private void populateUI(Movie movie) {
        String title = null;

        if (this.mDetailBinding == null) {
            Log.d("Populate UI", "null bindings : ");
            return;
        }

        if (movie != null) {
            Picasso.get().load(movie.getPosterPath()).error(R.drawable.image_placeholder).
                    into(this.mDetailBinding.movieDetail.ivMoviePosterDetail);
            Picasso.get().load(movie.getBackdropPath()).error(R.drawable.image_placeholder).
                    into(this.mDetailBinding.ivBackgroundDetail);

            this.setTitle(movie.getTitle());
            this.mDetailBinding.movieDetail.tvMovieDescription.setText(movie.getOverview());
            this.mDetailBinding.movieDetail.tvMovieDetailReleaseDate.setText(movie.getReleaseDate());
            this.mDetailBinding.movieDetail.tvMovieDetailRating.setText(Double.toString(movie.getVote()));
            this.mDetailBinding.movieDetail.tvMovieDetailTitle.setText(movie.getTitle());
        }
    }

    private class MovieTrailerPlayer implements AppAdapter.AppAdapterOnClickListener {
        public void onClick(int position) {
            Trailer trailer = mTrailerAdapter.getAdapterDataAt(position);

            if (trailer != null) {
                // make implicit intent
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
