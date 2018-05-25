package com.example.nebo.popular_movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.databinding.GridItemBinding;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;

abstract public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final AppAdapter.AppAdapterOnClickListener mListener;

    public MovieViewHolder(View itemView, AppAdapter.AppAdapterOnClickListener listener) {
        super(itemView);
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (this.mListener != null) {
            this.mListener.onClick(getAdapterPosition());
        }
    }

    abstract public void bind();

}
