package com.example.nebo.popular_movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.nebo.popular_movies.databinding.MovieDetailContentBinding;
import com.example.nebo.popular_movies.databinding.MovieDetailBinding;
import com.example.nebo.popular_movies.data.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private MovieDetailContentBinding mDetailBinding = null;
    private MovieDetailBinding mMovieBinding = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        this.mDetailBinding = DataBindingUtil.setContentView(this,
                R.layout.movie_detail_content);
        // this.mMovieBinding = DataBindingUtil.setContentView(this,
        //        R.layout.movie_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        Movie movie = null;

        if (intent != null) {
            movie = new Movie(intent.getStringExtra(getString(R.string.ik_movie_title)),
                    intent.getIntExtra(getString(R.string.ik_movie_id), -1),
                    intent.getDoubleExtra(getString(R.string.ik_user_rating), -1),
                    0.0,
                    intent.getStringExtra(getString(R.string.ik_movie_poster)),
                    intent.getStringExtra(getString(R.string.ik_movie_backdrop)),
                    intent.getStringExtra(getString(R.string.ik_movie_synopsis)),
                    intent.getStringExtra(getString(R.string.ik_release_date)));
        }

        this.populateUI(movie);
        Log.d("Movie Contents", movie.toString());
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
    }
}
