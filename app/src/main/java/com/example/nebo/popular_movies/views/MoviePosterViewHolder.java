package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.databinding.GridItemBinding;

public class MoviePosterViewHolder extends MovieViewHolder {
    private final GridItemBinding mBinding;

    public MoviePosterViewHolder(@NonNull GridItemBinding binding,
                                 @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        this.mBinding = binding;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bind(String resource) {

    }

    @Override
    public void onBindDefault() {

    }
}
