package com.example.nebo.popular_movies;

import android.view.View;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;

public class MovieReviewViewHolder extends MovieViewHolder {

    private static final int mLayoutID = R.layout.movie_review_item;
    private final MovieReviewItemBinding mBinding;

    public MovieReviewViewHolder(MovieReviewItemBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    @Override
    public android.databinding.ViewDataBinding getBinding() {
        return (android.databinding.ViewDataBinding) this.mBinding;
    }

    @Override
    public int getLayoutID() {
        return MovieReviewViewHolder.mLayoutID;
    }

    @Override
    public void onClick(View v) {

    }
}
