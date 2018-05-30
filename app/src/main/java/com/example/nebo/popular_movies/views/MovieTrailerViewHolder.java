package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.data.Review;
import com.example.nebo.popular_movies.data.Trailer;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;

public class MovieTrailerViewHolder <D>
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, MovieViewHolder<D> {

    private final MovieTrailerItemBinding mBinding;
    private final AppAdapter.AppAdapterOnClickListener mListener;

    public MovieTrailerViewHolder(@NonNull MovieTrailerItemBinding binding,
                                  @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot());
        this.mBinding = binding;
        this.mListener = listener;
    }

    @Override
    public void bind(D resource) {
        // @TODO cast handling, maybe operate on parceables since already supported?
        Trailer trailer = null;

        if (resource != null) {
            if (resource.getClass().equals(Review.class)) {
                trailer = (Trailer) resource;
            }
        }

        if (trailer != null) {
            // @TODO figure out what type of object this should be
            // this.mBinding.vvMovieTrailerItem.;
        }
    }

    @Override
    public void onBindDefault() {

    }

    @Override
    public void onClick(View v) {

    }
}
