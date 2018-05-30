package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;
import com.example.nebo.popular_movies.data.Review;

public class MovieReviewViewHolder <D>
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, MovieViewHolder<D> {

    private final MovieReviewItemBinding mBinding;
    private final AppAdapter.AppAdapterOnClickListener mListener;

    @Override
    public void onClick(View v) {
        if (this.mListener != null) {
            this.mListener.onClick(getAdapterPosition());
        }
    }

    public MovieReviewViewHolder(@NonNull MovieReviewItemBinding binding,
                                 @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot());
        this.mBinding = binding;
        this.mListener = listener;
        itemView.setOnClickListener(this);
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
