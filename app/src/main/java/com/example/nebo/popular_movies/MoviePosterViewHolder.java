package com.example.nebo.popular_movies;

import android.view.View;
import com.example.nebo.popular_movies.databinding.GridItemBinding;

public class MoviePosterViewHolder <B> extends MovieViewHolder {

    private static final int mLayoutID = R.layout.grid_item;
    private final GridItemBinding mBinding;

    public MoviePosterViewHolder(GridItemBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    @Override
    public android.databinding.ViewDataBinding getBinding() {
        return (android.databinding.ViewDataBinding) this.mBinding;
    }

    @Override
    public int getLayoutID() {
        return MoviePosterViewHolder.mLayoutID;
    }

    @Override
    public void onClick(View v) {

    }
}
