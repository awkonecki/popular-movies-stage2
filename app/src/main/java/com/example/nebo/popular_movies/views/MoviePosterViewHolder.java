package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.databinding.GridItemBinding;

public class MoviePosterViewHolder <D>
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, MovieViewHolder <D> {

    private final GridItemBinding mBinding;
    private final AppAdapter.AppAdapterOnClickListener mListener;

    public MoviePosterViewHolder(@NonNull GridItemBinding binding,
                                 @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot());
        this.mBinding = binding;
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bind(D resource) {

    }

    @Override
    public void onBindDefault() {

    }
}
