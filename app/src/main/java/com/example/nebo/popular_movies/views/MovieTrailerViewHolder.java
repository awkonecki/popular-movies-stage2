package com.example.nebo.popular_movies.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.nebo.popular_movies.AppAdapter;
import com.example.nebo.popular_movies.R;
import com.example.nebo.popular_movies.data.Review;
import com.example.nebo.popular_movies.data.Trailer;
import com.example.nebo.popular_movies.databinding.MovieTrailerItemBinding;
import com.squareup.picasso.Picasso;

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
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(D resource) {
        Log.d("Binder called", "binding the trailer resource.");

        // @TODO cast handling, maybe operate on parceables since already supported?
        Trailer trailer = null;

        if (resource != null) {
            if (resource.getClass().equals(Trailer.class)) {
                trailer = (Trailer) resource;
            }
        }

        if (trailer != null) {
            Log.d("Trailer Data", trailer.getmVideoImagePath());
            Picasso.get().load(trailer.getmVideoImagePath()).error(R.drawable.image_placeholder).
                    into(this.mBinding.ivMovieTrailerItem);
        }
    }

    @Override
    public void onBindDefault() {

    }

    @Override
    public void onClick(View v) {
        Trailer trailer = null;
        Log.d("Trailer OnClick", "Trailer onClick");
        this.mListener.onClick(getAdapterPosition());
    }
}
