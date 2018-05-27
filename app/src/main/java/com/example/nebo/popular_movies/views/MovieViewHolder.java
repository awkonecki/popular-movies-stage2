package com.example.nebo.popular_movies.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;


public interface MovieViewHolder<D> {
    void bind(D resource);
    void onBindDefault();
}

