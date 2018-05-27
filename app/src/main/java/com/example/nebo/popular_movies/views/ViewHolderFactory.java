package com.example.nebo.popular_movies.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.R;
import com.example.nebo.popular_movies.databinding.GridItemBinding;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;

public class ViewHolderFactory {
    private static ViewHolderFactory mFactoryInstance = null;

    private ViewHolderFactory() {

    }

    public static ViewHolderFactory getViewHolderFactory() {
        if (ViewHolderFactory.mFactoryInstance == null) {
            ViewHolderFactory.mFactoryInstance = new ViewHolderFactory();
        }

        return ViewHolderFactory.mFactoryInstance;
    }

    public static RecyclerView.ViewHolder createView(int layout,
                                          android.databinding.ViewDataBinding binding,
                                          AppAdapter.AppAdapterOnClickListener listener) {
        RecyclerView.ViewHolder viewHolder = null;
        switch(layout) {
            case R.layout.grid_item:
                viewHolder = new MoviePosterViewHolder((GridItemBinding) binding,
                        listener);
                break;
            case R.layout.movie_review_item:
                viewHolder = new MovieReviewViewHolder((MovieReviewItemBinding) binding,
                        listener);
                break;
            case R.layout.movie_trailer_item:
                viewHolder = new MovieTrailerViewHolder((MovieTrailerItemBinding) binding,
                        listener);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type support.");
        }



        return viewHolder;
    }
}
