package com.example.nebo.popular_movies;

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

    public static MovieViewHolder createView(MovieViewHolder view,
                                             android.databinding.ViewDataBinding binding) {
        if (view instanceof MoviePosterViewHolder) {
            return new MoviePosterViewHolder((GridItemBinding) binding);
        }
        else if (view instanceof MovieReviewViewHolder) {
            return new MovieReviewViewHolder((MovieReviewItemBinding) binding);
        }
        else if (view instanceof MovieTrailerViewHolder) {
            return new MovieTrailerViewHolder((MovieTrailerItemBinding) binding);
        }
        else {
            throw new IllegalArgumentException("Invalid view type support.");
        }
    }
}
