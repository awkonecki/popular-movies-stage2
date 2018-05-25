package com.example.nebo.popular_movies;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;

public class MovieReviewViewHolder extends MovieViewHolder {

    private final MovieReviewItemBinding mBinding;

    public MovieReviewViewHolder(@NonNull MovieReviewItemBinding binding,
                                 @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        this.mBinding = binding;
    }

    @Override
    public void bind() {

    }
}
