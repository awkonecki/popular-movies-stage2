package com.example.nebo.popular_movies;

import android.databinding.ViewDataBinding;
import android.view.View;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;

public class MovieTrailerViewHolder extends MovieViewHolder {

    private final MovieTrailerItemBinding mBinding;

    public MovieTrailerViewHolder(MovieTrailerItemBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getLayoutID() {
        return 0;
    }

    @Override
    public ViewDataBinding getBinding() {
        return null;
    }
}
