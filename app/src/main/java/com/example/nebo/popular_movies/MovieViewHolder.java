package com.example.nebo.popular_movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.databinding.GridItemBinding;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;

abstract public class MovieViewHolder <B> extends RecyclerView.ViewHolder implements View.OnClickListener {
    public MovieViewHolder(View itemView) {
        super(itemView);
    }

    public MovieViewHolder(GridItemBinding binding) {
        super(binding.getRoot());
    }

    public MovieViewHolder(MovieReviewItemBinding binding) {
        super(binding.getRoot());
    }

    public MovieViewHolder(MovieTrailerItemBinding binding) {
        super(binding.getRoot());
    }

    @Override
    abstract public void onClick(View v);

    abstract public int getLayoutID();
    abstract public android.databinding.ViewDataBinding getBinding();
}
