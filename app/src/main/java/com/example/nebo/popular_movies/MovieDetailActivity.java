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
import com.example.nebo.popular_movies.data.Review;
import com.example.nebo.popular_movies.data.Trailer;
import com.example.nebo.popular_movies.databinding.MovieDetailBinding;
import com.example.nebo.popular_movies.databinding.MovieDetailContentBinding;
import com.example.nebo.popular_movies.data.Movie;
import com.example.nebo.popular_movies.util.JsonUtils;
import com.example.nebo.popular_movies.views.MovieReviewViewHolder;
import com.example.nebo.popular_movies.views.MovieTrailerViewHolder;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private MovieDetailBinding mDetailBinding = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.movie_detail);
        // this.mDetailBinding = DataBindingUtil.setContentView(this,
        //        R.layout.movie_detail_content);
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
        // @TODO Provide a listener for the trailer (implicit intent).
        AppAdapter<Review, MovieReviewViewHolder<Review>> reviewAdapter =
                new AppAdapter<Review, MovieReviewViewHolder<Review>>(null,
                R.layout.movie_review_item);
        AppAdapter<Trailer, MovieTrailerViewHolder<Trailer>> trailerAdapter =
                new AppAdapter<Trailer, MovieTrailerViewHolder<Trailer>>(null,
                R.layout.movie_trailer_item);

        // 3. Adding of the layout manager to the recycler views.
        this.mDetailBinding.movieDetail.rvMovieDetailReviews.setLayoutManager(reviewLayoutManager);
        this.mDetailBinding.movieDetail.rvMovieDetailTrailers.setLayoutManager(trailerLayoutManager);

        // 4. Adding of the adapters to the recycler views.
        this.mDetailBinding.movieDetail.rvMovieDetailReviews.setAdapter(reviewAdapter);
        this.mDetailBinding.movieDetail.rvMovieDetailTrailers.setAdapter(trailerAdapter);

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

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> reviewTrailerLoader = loaderManager.getLoader(1);

        if (loaderManager == null) {
            Log.d("LoaderManagerInfo", "Null manager");
        }

        Bundle args = new Bundle();
        args.putInt(getString(R.string.bk_page_number), 1);
        args.putString(getString(R.string.bk_request_type), getString(R.string.bv_request_type_trailers));
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
                    into(this.mDetailBinding.movieDetail.ivMoivePosterDetail);
            // Picasso.get().load(movie.getBackdropPath()).error(R.drawable.image_placeholder).
            //        into(this.mMovieBinding.ivBackgroundDetail);

            this.setTitle(movie.getTitle());
            this.mDetailBinding.movieDetail.tvMovieDescription.setText(movie.getOverview());
            this.mDetailBinding.movieDetail.tvMovieDetailReleaseDate.setText(movie.getReleaseDate());
            this.mDetailBinding.movieDetail.tvMovieDetailRating.setText(Double.toString(movie.getVote()));
            this.mDetailBinding.movieDetail.tvMovieDetailTitle.setText(movie.getTitle());
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
        if (data != null) {
            // @TODO fix the case one day likely with an interface for the binding instead of super
            // class.
            AppAdapter<Review, MovieReviewViewHolder<Review>> reviewAdapter =
                    (AppAdapter<Review, MovieReviewViewHolder<Review>>)
                            this.mDetailBinding.movieDetail.rvMovieDetailReviews.getAdapter();
            reviewAdapter.setAdapterData(JsonUtils.parseJsonResponseForReviews(data));
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<String> loader) {

    }
}
