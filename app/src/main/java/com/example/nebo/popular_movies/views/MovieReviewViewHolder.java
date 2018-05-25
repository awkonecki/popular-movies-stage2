package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;

public class MovieReviewViewHolder extends MovieViewHolder {

    private final MovieReviewItemBinding mBinding;

    public MovieReviewViewHolder(@NonNull MovieReviewItemBinding binding,
                                 @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        this.mBinding = binding;
    }

    @Override
    public void bind(String resource) {

    }

    @Override
    public void onBindDefault() {

    }
}
