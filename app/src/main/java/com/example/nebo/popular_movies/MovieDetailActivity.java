package com.example.nebo.popular_movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.nebo.popular_movies.databinding.MovieDetailContentBinding;
import com.example.nebo.popular_movies.databinding.MovieDetailBinding;
import com.example.nebo.popular_movies.data.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

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
    }

    private void populateUI(Movie movie) {
        MovieDetailContentBinding detailBinding = DataBindingUtil.getBinding(this.getCurrentFocus());
        MovieDetailBinding movieBinding = DataBindingUtil.getBinding(this.getCurrentFocus());
        String title = null;

        if (movie != null) {
            Picasso.get().load(movie.getPosterPath()).error(R.drawable.image_placeholder).
                    into(detailBinding.ivMoivePosterDetail);
            Picasso.get().load(movie.getBackdropPath()).error(R.drawable.image_placeholder).
                    into(movieBinding.ivBackgroundDetail);

            if (movie.getTitle() == null) {
                title = getString(R.string.default_title);
            }

            this.setTitle(title);
            detailBinding.tvLabelSummary.setText(movie.getOverview());
            detailBinding.tvLabelReleaseDate.setText(movie.getReleaseDate());
            detailBinding.tvMovieDetailRating.setText(Double.toString(movie.getVote()));
            detailBinding.tvLabelTitle.setText(movie.getTitle());
        }
    }
}
