package com.example.nebo.popular_movies.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;

abstract public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final AppAdapter.AppAdapterOnClickListener mListener;

    public MovieViewHolder(View itemView, AppAdapter.AppAdapterOnClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (this.mListener != null) {
            this.mListener.onClick(getAdapterPosition());
        }
    }

    abstract public void bind(String resource);
    abstract public void onBindDefault();

}
