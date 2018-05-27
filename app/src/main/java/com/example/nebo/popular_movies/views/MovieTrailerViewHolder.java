package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;

public class MovieTrailerViewHolder <D> extends MovieViewHolder<D> {

    private final MovieTrailerItemBinding mBinding;

    public MovieTrailerViewHolder(@NonNull MovieTrailerItemBinding binding,
                                  @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        this.mBinding = binding;
    }

    @Override
    public void bind(D resource) {
        // this.mBinding.vvMovieTrailerItem.set
    }

    @Override
    public void onBindDefault() {

    }
}
