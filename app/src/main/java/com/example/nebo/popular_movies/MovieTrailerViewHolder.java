package com.example.nebo.popular_movies;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;

public class MovieTrailerViewHolder extends MovieViewHolder {

    private final MovieTrailerItemBinding mBinding;

    public MovieTrailerViewHolder(@NonNull MovieTrailerItemBinding binding,
                                  @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        this.mBinding = binding;
    }

    @Override
    public void bind() {
        // this.mBinding.vvMovieTrailerItem.set
    }
}
