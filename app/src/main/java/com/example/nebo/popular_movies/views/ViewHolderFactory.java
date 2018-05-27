package com.example.nebo.popular_movies.views;

import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.R;
import com.example.nebo.popular_movies.databinding.GridItemBinding;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;

public class ViewHolderFactory {
    private static ViewHolderFactory mFactoryInstance = null;
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


    private ViewHolderFactory() {

    }

    public static ViewHolderFactory getViewHolderFactory() {
        if (ViewHolderFactory.mFactoryInstance == null) {
            ViewHolderFactory.mFactoryInstance = new ViewHolderFactory();
        }

        return ViewHolderFactory.mFactoryInstance;
    }

    public static MovieViewHolder createView(int layout,
                                             android.databinding.ViewDataBinding binding,
                                             AppAdapter.AppAdapterOnClickListener listener) {
        MovieViewHolder movieViewHolder = null;
        switch(layout) {
            case R.layout.grid_item:
                movieViewHolder = new MoviePosterViewHolder((GridItemBinding) binding,
                        listener);
                break;
            case R.layout.movie_review_item:
                movieViewHolder = new MovieReviewViewHolder((MovieReviewItemBinding) binding,
                        listener);
            case R.layout.movie_trailer_item:
                movieViewHolder = new MovieTrailerViewHolder((MovieTrailerItemBinding) binding,
                        listener);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type support.");
        }



        return movieViewHolder;
    }
}
