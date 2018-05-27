package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;
import com.example.nebo.popular_movies.data.Review;

public class MovieReviewViewHolder <D> extends MovieViewHolder <D> {

    private final MovieReviewItemBinding mBinding;

    public MovieReviewViewHolder(@NonNull MovieReviewItemBinding binding,
                                 @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        this.mBinding = binding;
    }

    @Override
    public void bind(D resource) {
        // @TODO cast handling, maybe operate on parceables since already supported?
        Review review = null;

        if (resource != null) {
            if (resource.getClass().equals(Review.class)) {
                review = (Review) resource;
            }
        }

        if (review != null) {
            this.mBinding.tvReivew.setText(review.getContent());
        }
    }

    @Override
    public void onBindDefault() {

    }
}
