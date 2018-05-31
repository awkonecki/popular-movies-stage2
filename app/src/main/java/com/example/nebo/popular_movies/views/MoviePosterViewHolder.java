package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.R;
import com.example.nebo.popular_movies.data.Movie;
import com.example.nebo.popular_movies.data.Review;
import com.example.nebo.popular_movies.databinding.GridItemBinding;
import com.squareup.picasso.Picasso;

public class MoviePosterViewHolder <D>
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, MovieViewHolder <D> {

    private final GridItemBinding mBinding;
    private final AppAdapter.AppAdapterOnClickListener mListener;

    public MoviePosterViewHolder(@NonNull GridItemBinding binding,
                                 @NonNull AppAdapter.AppAdapterOnClickListener listener) {
        super(binding.getRoot());
        this.mBinding = binding;
        itemView.setOnClickListener(this);
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        this.mListener.onClick(getAdapterPosition());
    }

    @Override
    public void bind(D resource) {
        Movie movie = null;

        if (resource != null) {
            if (resource.getClass().equals(Movie.class)) {
                movie = (Movie) resource;
            }
        }

        if (movie != null) {
            Picasso.get().
                    load(movie.getPosterPath()).
                    error(R.drawable.image_placeholder).
                    into(this.mBinding.ivMoviePoster);
        }
    }

    @Override
    public void onBindDefault() {

    }
}
