package com.example.nebo.popular_movies;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nebo.popular_movies.databinding.GridItemBinding;
import com.example.nebo.popular_movies.databinding.MovieReviewItemBinding;

import java.util.List;

// new AppAdapter<Movie, MoviePosterViewHolder>

public class AppAdapter <D, VH extends MovieViewHolder> extends RecyclerView.Adapter<MovieViewHolder> {

    private final AppAdapter.AppAdapterOnClickListener mListener;
    private final Class<VH> mViewHolder;
    private List<D> mAdapterData = null;
    private int mLayout;

    public AppAdapter(AppAdapter.AppAdapterOnClickListener listener, Class<VH> viewHolder) {
        this.mListener = listener;
        this.mViewHolder = viewHolder;
    }

    public interface AppAdapterOnClickListener {
        public void onClick(int position);
    }

    public void setAdapterData(List<D> adapterData) {
        this.mAdapterData = adapterData;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return ViewHolderFactory.createView(this.mViewHolder,
                DataBindingUtil.inflate(layoutInflater,
                    this.mViewHolder.getLayoutID(),
                    parent,
                    false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (this.mAdapterData == null) {
            return 0;
        }
        return this.mAdapterData.size();
    }
}
