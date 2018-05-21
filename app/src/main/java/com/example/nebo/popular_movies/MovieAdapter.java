package com.example.nebo.popular_movies;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nebo.popular_movies.async.MovieManagedData;
import com.example.nebo.popular_movies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;
import com.example.nebo.popular_movies.databinding.GridItemBinding;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // Cache the instance of the onClickListener desired by the application.
    private MovieAdatperOnClickListener mMovieAdatperOnClickListener = null;
    private List<Movie> mMovies = null;
    private MovieManagedData mManagedMovies = null;

    public MovieAdapter(MovieAdatperOnClickListener listener) {
        this.mMovieAdatperOnClickListener = listener;
    }

    public interface MovieAdatperOnClickListener {
        void OnClick(int position);
    }

    public MovieManagedData getMovieData() {
        return this.mManagedMovies;
    }

    public void setMovieData(MovieManagedData managedMovies) {
        this.mManagedMovies = managedMovies;
        if (this.mManagedMovies != null) {
            this.setMovies(this.mManagedMovies.getMovies());
        }
    }

    private void setMovies(List<Movie> movies) {
        this.mMovies = movies;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // Required for inheritance from RecyclerView.Adapter due to abstract definition.
        if (this.mMovies == null) {
            return 0;
        }
        else {
            return this.mMovies.size();
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Required for inheritance from RecyclerView.Adapter due to abstract definition.

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        GridItemBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.grid_item,
                parent,
                false);

        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (this.mMovies != null && position < this.mMovies.size()) {
            holder.bind(this.mMovies.get(position).getPosterPath());
        }
        else {
            holder.bind("junk");
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final GridItemBinding mBinding;

        MovieViewHolder(GridItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            itemView.setOnClickListener(this);
        }

        void bind(String imageURL) {
            Picasso.get().load(imageURL).error(R.drawable.image_placeholder).into(this.mBinding.ivMoviePoster);
        }

        @Override
        public void onClick(View v) {
            MovieAdapter.this.onClick(getAdapterPosition());
        }
    }

    private void onClick(int position) {
        if (this.mMovieAdatperOnClickListener != null) {
            this.mMovieAdatperOnClickListener.OnClick(position);
        }
    }
}
